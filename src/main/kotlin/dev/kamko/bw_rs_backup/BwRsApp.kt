package dev.kamko.bw_rs_backup

import dev.kamko.bw_rs_backup.bw.BitwardenBackup
import dev.kamko.bw_rs_backup.scheduling.QuartzScheduler
import dev.kamko.bw_rs_backup.storage.CloudStorage
import dev.kamko.bw_rs_backup.storage.b2.B2Storage
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("dev.kamko.bw_rs_backup.main")

val scheduler = QuartzScheduler()

fun main() {
    log.info("Welcome to bw_rs-backup")
    val appConfig = loadAppConfig()

    val storage: CloudStorage = B2Storage(config = appConfig.b2Config)
    val bwBackup = BitwardenBackup(
        storage = storage,
        password = appConfig.zipPassword
    )

    log.info("Scheduling backup job with cron expression: ${appConfig.cron}")
    scheduler.scheduleJob(Runnable { bwBackup.createBackup() }, appConfig.cron)
}

