package io.ankburov.kotlinxsltdsl.integration

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.utils.bodyNotNull
import io.ankburov.kotlinxsltdsl.utils.ok
import io.ankburov.kotlinxsltdsl.utils.send
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchemaEndpointIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun getAvailableSchemas() {
        val schemas = restTemplate.send<Array<Schema>>("/schema", HttpMethod.GET)
                .ok()
                .bodyNotNull()

        assertArrayEquals(Schema.values(), schemas)
    }

    @Test
    fun getSchemaExpectedNodes() {
        val expectedSchema = Schema.XML_EXAMPLE_V1
        val nodes = restTemplate.send<List<String>>("/schema/$expectedSchema/nodes", HttpMethod.GET)
                .ok()
                .bodyNotNull()

        assertEquals(listOf("year", "brand", "model", "something/nested"), nodes)
    }
}