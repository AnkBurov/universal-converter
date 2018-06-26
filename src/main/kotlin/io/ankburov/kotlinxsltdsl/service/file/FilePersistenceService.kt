package io.ankburov.kotlinxsltdsl.service.file

import java.nio.file.Path
import java.util.*

interface FilePersistenceService {

    fun uploadFile(tempFile: Path): UUID


}