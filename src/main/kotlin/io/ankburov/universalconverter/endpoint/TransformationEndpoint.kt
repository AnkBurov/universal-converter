package io.ankburov.universalconverter.endpoint

import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.model.TransformationRequest
import io.ankburov.universalconverter.service.TransformationService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@RestController
@RequestMapping("/transform")
class TransformationEndpoint(
        private val transformationService: TransformationService
) {

    @PostMapping("{schema}")
    fun transformFile(@PathVariable schema: Schema, @RequestBody request: TransformationRequest): Mono<List<String>> {
        require(request.mappings.isNotEmpty()) { "Mapping for scheme ${schema.name} cannot be empty" }

        return Mono.just(request)
                .map { transformationService.transformFile(schema, it.fileUUID, it.mappings, it.properties) }
                .subscribeOn(Schedulers.elastic())
    }
}