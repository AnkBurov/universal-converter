package io.ankburov.kotlinxsltdsl.utils

import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

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