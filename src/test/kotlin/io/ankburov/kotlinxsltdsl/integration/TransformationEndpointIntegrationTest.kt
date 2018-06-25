package io.ankburov.kotlinxsltdsl.integration

import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.junit4.SpringRunner
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransformationEndpointIntegrationTest {

    private val expectedFile = ClassPathResource("samples/nested.csv")

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun transformFile() {
    }
}