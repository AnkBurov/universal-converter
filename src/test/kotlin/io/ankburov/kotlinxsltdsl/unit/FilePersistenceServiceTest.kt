package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.service.file.FilePersistenceService
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FilePersistenceServiceTest {

    @Autowired
    private lateinit var filePersistenceService: FilePersistenceService

    @Test
    fun uploadFile() {
        val csv = ClassPathResource("samples/nested.csv")

        val fileUuid = filePersistenceService.uploadFile(csv.file.toPath())

        assertNotNull(fileUuid)
    }
}