package io.ankburov.kotlinxsltdsl.utils

import io.ankburov.kotlinxsltdsl.model.NodeName
import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType

fun String.execute(): Process {
    return Runtime.getRuntime().exec(this)
}

fun String.toAbsolutePath() = Paths.get(this).toAbsolutePath().toString()

fun Path.absolutePath() = this.toAbsolutePath().toString()

fun Flux<DataBuffer>.writeToTempFile(): Mono<Path> {
    val tempDirectory = Files.createTempDirectory("upload_orig")
    val tempFile = Files.createTempFile(tempDirectory, "orig", "")
    Files.newByteChannel(tempFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { byteChannel ->
        this.map(DataBuffer::asByteBuffer)
                .map(byteChannel::write)
                .subscribe()
    }
    return tempFile.toMono()
}

fun <T> Collection<T>.startsWith(predicate: (T) -> Boolean) = this.isNotEmpty() && predicate(this.iterator().next())

inline fun <reified T : Any> T.getNodeNameFields(): List<NodeName> {
    return this::class
            .declaredMemberProperties
            .filter { it.returnType == NodeName::class.starProjectedType }
            .map { it as KProperty1<T, NodeName> }
            .map { it.get(this) }
            .also { nodeNames ->
                require(nodeNames.isNotEmpty()) { "${this::class.simpleName} cannot have absent empty using node names field" }
            }
}