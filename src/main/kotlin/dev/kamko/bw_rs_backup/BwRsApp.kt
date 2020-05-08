package dev.kamko.bw_rs_backup

import dev.kamko.bw_rs_backup.bw.BitwardenBackup
import dev.kamko.bw_rs_backup.scheduling.QuartzScheduler
import dev.kamko.bw_rs_backup.storage.CloudStorage
import dev.kamko.bw_rs_backup.storage.b2.B2Storage

val scheduler = QuartzScheduler()

fun main() {
    val appConfig = loadAppConfig()

    val storage: CloudStorage = B2Storage(config = appConfig.b2Config)
    val bwBackup = BitwardenBackup(
        storage = storage,
        password = appConfig.zipPassword
    )

    scheduler.scheduleJob(Runnable { bwBackup.createBackup() }, appConfig.cron)
}

