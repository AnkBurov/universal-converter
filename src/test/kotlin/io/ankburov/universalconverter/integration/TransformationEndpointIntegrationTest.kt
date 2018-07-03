package io.ankburov.universalconverter.integration

import io.ankburov.universalconverter.model.FileInfo
import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.model.TransformationRequest
import io.ankburov.universalconverter.utils.Constants
import io.ankburov.universalconverter.utils.TestConstants.Companion.EXPECTED_XML
import io.ankburov.universalconverter.utils.bodyNotNull
import io.ankburov.universalconverter.utils.generateBody
import io.ankburov.universalconverter.utils.ok
import io.ankburov.universalconverter.utils.send
import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner
import org.xmlunit.builder.Input
import org.xmlunit.diff.DOMDifferenceEngine

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransformationEndpointIntegrationTest {

    private val expectedFile = ClassPathResource("samples/nested.csv")

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun transformFile() {
        val (fileUuid, _) = restTemplate.postForEntity("/upload", generateBody(expectedFile), FileInfo::class.java)
                .ok()
                .bodyNotNull()

        val request = TransformationRequest(fileUuid, getMappings(), getProperties())


        val xmls = restTemplate.send<List<String>>("/transform/${Schema.XML_EXAMPLE_V1}", HttpMethod.POST, HttpEntity(request))
                .ok()
                .bodyNotNull()

        assertEquals(1, xmls.size)
        DOMDifferenceEngine().compare(EXPECTED_XML.xmlUnit(), xmls[0].xmlUnit())
    }

    private fun getMappings(): Map<String, String> {
        return mapOf("year" to "year",
                "brand" to "brand",
                "model" to "model",
                "something/nested" to "something/nested")
    }

    private fun getProperties(): Map<String, String> {
        return mapOf(Constants.IS_FIRST_ROW_A_HEADER to "true")
    }

    private fun String.xmlUnit() = Input.fromString(this).build()
}