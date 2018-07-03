package io.ankburov.kotlinxsltdsl.endpoint

import io.ankburov.kotlinxsltdsl.model.NodeName
import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.service.dst.DestinationConverterService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/schema")
class SchemaEndpoint(
        private val destinationConverterService: DestinationConverterService
) {

    @GetMapping
    fun getAvailableSchemas() = destinationConverterService.getAvailableSchemas()

    @GetMapping("{schema}/nodes")
    fun getSchemaExpectedNodes(@PathVariable schema: Schema): List<NodeName> {
        return destinationConverterService.getConverterStrategy(schema).getUsingNodeNames()
    }
}