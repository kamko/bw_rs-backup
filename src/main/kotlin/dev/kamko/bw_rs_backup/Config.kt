package dev.kamko.bw_rs_backup

import dev.kamko.bw_rs_backup.notification.TelegramConfig
import dev.kamko.bw_rs_backup.storage.b2.B2Config
import java.util.*

const val USER_AGENT = "kamko/bw_rs-backup"

data class AppConfig(
    val zipPassword: String,
    val b2Config: B2Config,
    val telegram: TelegramConfig,
    val cron: String
)

fun loadAppConfig() = AppConfig(
    zipPassword = System.getenv("BACKUP_ZIP_PASSWORD"),
    b2Config = B2Config(
        keyId = System.getenv("BACKUP_B2_KEY_ID"),
        keyValue = System.getenv("BACKUP_B2_KEY_VALUE"),
        bucketId = System.getenv("BACKUP_B2_BUCKET_ID")
    ),
    telegram = TelegramConfig(
        botToken = System.getenv("BACKUP_TELEGRAM_TOKEN"),
        chatId = System.getenv("BACKUP_TELEGRAM_CHAT_ID")
    ),
    cron = System.getenv("BACKUP_CRON")
)

data class GitProperties(
    val commit: String,
    val version: String
)

fun loadGitProperties(): GitProperties {
    BwRsApp::class.java.getResourceAsStream("/git.properties").use {
        val props = Properties()
        props.load(it)

        return GitProperties(
            commit = props["git.commit.id"] as String,
            version = props["git.build.version"] as String
        )
    }
}
