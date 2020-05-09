package dev.kamko.bw_rs_backup

import dev.kamko.bw_rs_backup.bitwarden.BackupJob
import dev.kamko.bw_rs_backup.bitwarden.BackupProvider
import dev.kamko.bw_rs_backup.notification.TelegramNotifier
import dev.kamko.bw_rs_backup.scheduling.QuartzScheduler
import dev.kamko.bw_rs_backup.storage.b2.B2Storage
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(BwRsApp::class.java)

class BwRsApp {

    private val appConfig = loadAppConfig()
    private val scheduler = QuartzScheduler(
        TelegramNotifier(appConfig.telegram)
    )

    fun run() {
        log.info("Welcome to bw_rs-backup")

        val bwBackup = BackupJob(
            storage = B2Storage(config = appConfig.b2Config),
            provider = BackupProvider(appConfig.zipPassword)
        )
        scheduleBackup(bwBackup)
    }

    private fun scheduleBackup(job: BackupJob) {
        log.info("Scheduling backup job with cron expression: ${appConfig.cron}")
        scheduler.scheduleJob(Runnable { job.runBackup() }, appConfig.cron)
    }
}


fun main() {
    BwRsApp().run()
}
