package io.ankburov.kotlinxsltdsl.integration

import io.ankburov.kotlinxsltdsl.model.FileInfo
import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.model.TransformationRequest
import io.ankburov.kotlinxsltdsl.utils.Constants
import io.ankburov.kotlinxsltdsl.utils.TestConstants.Companion.EXPECTED_XML
import io.ankburov.kotlinxsltdsl.utils.bodyNotNull
import io.ankburov.kotlinxsltdsl.utils.generateBody
import io.ankburov.kotlinxsltdsl.utils.ok
import io.ankburov.kotlinxsltdsl.utils.send
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

        val request = TransformationRequest(fileUuid, getMappings())


        val xmls = restTemplate.send<List<String>>("/transform/${Schema.XML_EXAMPLE_V1}", HttpMethod.POST, HttpEntity(request))
                .ok()
                .bodyNotNull()

        assertEquals(1, xmls.size)
        assertEquals(EXPECTED_XML, xmls[0])
    }

    private fun getMappings(): Map<String, String> {
        return mapOf("year" to "year",
                "brand" to "brand",
                "model" to "model",
                "something/nested" to "something/nested",
                Constants.IS_FIRST_ROW_A_HEADER to "true")
    }
}