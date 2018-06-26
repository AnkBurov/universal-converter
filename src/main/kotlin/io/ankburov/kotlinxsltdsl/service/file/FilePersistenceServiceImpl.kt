package io.ankburov.kotlinxsltdsl.service.file

import org.apache.ignite.IgniteCache
import org.springframework.stereotype.Service
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
}