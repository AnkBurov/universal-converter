package io.ankburov.universalconverter.integration

import io.ankburov.universalconverter.model.FileInfo
import io.ankburov.universalconverter.utils.bodyNotNull
import io.ankburov.universalconverter.utils.generateBody
import io.ankburov.universalconverter.utils.ok
import org.apache.ignite.IgniteCache
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadFileEndpointTest {

    private val expectedFile = ClassPathResource("samples/nested.csv")

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var fileCache: IgniteCache<UUID, ByteArray>

    @Test
    fun uploadFile() {
        val (fileUuid, headers) = restTemplate.postForEntity("/upload", generateBody(expectedFile), FileInfo::class.java)
                .ok()
                .bodyNotNull()

        assertNotNull(fileUuid)
        assertEquals(listOf("year", "brand", "model", "something/nested"), headers)

        assertNotNull(fileCache.getAndRemove(fileUuid))
    }
}