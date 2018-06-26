package io.ankburov.kotlinxsltdsl.endpoint

import io.ankburov.kotlinxsltdsl.model.FileInfo
import io.ankburov.kotlinxsltdsl.service.file.FilePersistenceService
import io.ankburov.kotlinxsltdsl.service.src.SourceFileParserTreeService
import io.ankburov.kotlinxsltdsl.utils.writeToTempFile
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
@RequestMapping("/upload")
class UploadFileEndpoint(
        private val filePersistenceService: FilePersistenceService,
        private val sourceFileParserTreeService: SourceFileParserTreeService
) {

    @PostMapping
    fun uploadFile(@RequestPart("file") file: FilePart): Mono<FileInfo> {

        return file.content()
                .writeToTempFile()
                .map { tmpFile ->
                    val fileUuid = filePersistenceService.uploadFile(tmpFile)
                    val headers = sourceFileParserTreeService.parseHeaders(tmpFile)
                    FileInfo(fileUuid, headers)
                }
                .subscribeOn(Schedulers.elastic())
    }
}