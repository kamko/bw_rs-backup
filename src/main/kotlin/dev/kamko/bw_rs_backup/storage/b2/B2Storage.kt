package dev.kamko.bw_rs_backup.storage.b2

import com.backblaze.b2.client.B2StorageClientFactory
import com.backblaze.b2.client.contentSources.B2FileContentSource
import com.backblaze.b2.client.structures.B2UploadFileRequest
import dev.kamko.bw_rs_backup.USER_AGENT
import dev.kamko.bw_rs_backup.storage.CloudStorage
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path


data class B2Config(val keyId: String, val keyValue: String, val bucketId: String)

class B2Storage(val config: B2Config) : CloudStorage {

    private val client = B2StorageClientFactory
        .createDefaultFactory()
        .create(config.keyId, config.keyValue, USER_AGENT)

    override fun save(name: String, contentType: String, content: InputStream) {
        val tmpFile = saveToTempFile(content)
        client.uploadSmallFile(
            B2UploadFileRequest.builder(
                config.bucketId,
                name,
                contentType,
                B2FileContentSource.build(tmpFile.toFile())
            ).build()
        )

        deleteFile(tmpFile)
    }

    private fun saveToTempFile(inputStream: InputStream): Path {
        val tempFile = Files.createTempFile("os", ".b2.tmp")
        Files.newOutputStream(tempFile).use { os -> inputStream.transferTo(os) }
        return tempFile
    }

    private fun deleteFile(path: Path) {
        Files.delete(path)
    }
}
