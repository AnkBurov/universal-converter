package io.ankburov.universalconverter.service.file

import java.io.Reader
import java.nio.file.Path
import java.util.*

interface FilePersistenceService {

    fun uploadFile(tempFile: Path): UUID

    fun getFileReader(fileUuid: UUID): Reader
}