package io.ankburov.kotlinxsltdsl.config

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.Ignition
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.events.EventType
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import java.util.concurrent.TimeUnit
import javax.cache.expiry.AccessedExpiryPolicy
import javax.cache.expiry.Duration
import org.apache.ignite.configuration.DataStorageConfiguration

@Configuration
class MainConfiguration {

    @Value("\${cache.files.expiration.minutes:240}")
    private var fileCacheExpiration: Long? = null

    @Value("\${cache.ignite.storagePath:#{null}}")
    private var igniteStoragePath: String? = null

    @Value("\${cache.ignite.walPath:#{null}}")
    private var igniteWalPath: String? = null

    @Value("\${cache.ignite.walArchivePath:#{null}}")
    private var igniteWalArchivePath: String? = null

    @Bean
    fun igniteConfiguration(): IgniteConfiguration {
        val configuration = IgniteConfiguration()

        val multicastIpFinder = TcpDiscoveryMulticastIpFinder()
        multicastIpFinder.multicastGroup = "228.10.10.200"

        val tcpDiscoverySpi = TcpDiscoverySpi()
        tcpDiscoverySpi.ipFinder = multicastIpFinder
        configuration.discoverySpi = tcpDiscoverySpi

        configuration.setIncludeEventTypes(EventType.EVT_TASK_STARTED, EventType.EVT_TASK_FINISHED, EventType.EVT_TASK_FAILED)

        // Ignite persistence configuration.
        val storageCfg = DataStorageConfiguration()

        igniteStoragePath?.let { storageCfg.storagePath = it }
        igniteWalPath?.let { storageCfg.walPath = it }
        igniteWalArchivePath?.let { storageCfg.walArchivePath = it }

        // Enabling the persistence.
        storageCfg.defaultDataRegionConfiguration.isPersistenceEnabled = true
        configuration.dataStorageConfiguration = storageCfg

        return configuration
    }

    @Bean
    fun ignite(igniteConfiguration: IgniteConfiguration): Ignite {
        val ignite = Ignition.getOrStart(igniteConfiguration)

        val cluster = ignite.cluster()
        // Getting all the server nodes that are already up and running.
        val nodes = cluster.forServers().nodes()

        if (!cluster.active()) cluster.active()

        // Setting the baseline topology that is represented by these nodes.
        cluster.setBaselineTopology(nodes)
        return ignite
    }


    @Bean
    fun fileCache(ignite: Ignite): IgniteCache<UUID, ByteArray> {
        val cacheConfiguration = CacheConfiguration<UUID, ByteArray>()
        cacheConfiguration.setName("fileCache")
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED)

        val expirationFactory = AccessedExpiryPolicy.factoryOf(Duration(TimeUnit.MINUTES, fileCacheExpiration!!))
        cacheConfiguration.setExpiryPolicyFactory(expirationFactory)

        return ignite.getOrCreateCache(cacheConfiguration)
    }
}