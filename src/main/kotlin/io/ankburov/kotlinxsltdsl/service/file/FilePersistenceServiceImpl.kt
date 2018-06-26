package io.ankburov.kotlinxsltdsl.service.file

import org.apache.ignite.IgniteCache
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

@Service
class FilePersistenceServiceImpl(
        private val fileCache: IgniteCache<UUID, ByteArray>
) : FilePersistenceService {

    override fun uploadFile(tempFile: Path): UUID {
        val fileUuid = UUID.randomUUID()
        val fileContent = Files.readAllBytes(tempFile)
        fileCache.put(fileUuid, fileContent)
        return fileUuid
    }

    override fun getFileReader(fileUuid: UUID): Reader {
        //todo fix get apache ignite assertion
        val bytes = fileCache.getAndRemove(fileUuid) ?: throw IllegalArgumentException("No files with uuid $fileUuid")
        return BufferedReader(InputStreamReader(ByteArrayInputStream(bytes)))
    }
}