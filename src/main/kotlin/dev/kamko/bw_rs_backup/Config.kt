package dev.kamko.bw_rs_backup

import dev.kamko.bw_rs_backup.storage.b2.B2Config

const val USER_AGENT = "kamko/bw_rs-backup" // todo: add version

data class AppConfig(
    val zipPassword: String,
    val b2Config: B2Config,
    val cron: String
)

fun loadAppConfig() = AppConfig(
    zipPassword = System.getenv("BACKUP_ZIP_PASSWORD"),
    b2Config = B2Config(
        keyId = System.getenv("BACKUP_B2_KEY_ID"),
        keyValue = System.getenv("BACKUP_B2_KEY_VALUE"),
        bucketId = System.getenv("BACKUP_B2_BUCKET_ID")
    ),
    cron = System.getenv("BACKUP_CRON")
)
