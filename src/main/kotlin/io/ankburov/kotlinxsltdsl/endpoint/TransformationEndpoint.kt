package io.ankburov.kotlinxsltdsl.endpoint

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.service.TransformationService
import io.ankburov.kotlinxsltdsl.utils.writeToTempFile
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
@RequestMapping("/transform")
class TransformationEndpoint(
        private val transformationService: TransformationService
) {

    @PostMapping("{schema}")
    fun transformFile(@RequestPart("file") file: FilePart, @PathVariable schema: Schema, mappings: Map<String, String>): Mono<List<String>> {
        require(mappings.isNotEmpty()) { "Mapping for scheme ${schema.name} cannot be empty" }

        return file.content()
                .writeToTempFile()
                .map { transformationService.transformFile(it, schema, mappings) }
                .subscribeOn(Schedulers.elastic())
    }
}