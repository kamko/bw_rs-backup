package dev.kamko.bw_rs_backup.bitwarden

import dev.kamko.bw_rs_backup.storage.CloudStorage
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

private val log = LoggerFactory.getLogger(BackupJob::class.java)

class BackupJob(
    private val provider: BackupProvider,
    private val storage: CloudStorage
) {

    fun runBackup() {
        log.info("Running backup job")
        provider.createZip().use {
            storage.save(
                name = filename(),
                contentType = "application/zip",
                content = it
            )
        }
    }

    private fun filename() = "bitwarden_backup_${LocalDateTime.now()}-encrypted.zip"
}
