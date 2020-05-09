package dev.kamko.bw_rs_backup.scheduling

import dev.kamko.bw_rs_backup.notification.Notifier
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

private val log = LoggerFactory.getLogger(QuartzScheduler::class.java)

class QuartzScheduler(private val errorNotifier: Notifier) {

    private val sf = StdSchedulerFactory()
    private val scheduler = sf.scheduler

    fun scheduleJob(runnable: Runnable, cron: String) {
        val job = JobBuilder.newJob(RunnableJob::class.java)
            .setJobData(
                JobDataMap(
                    mapOf(
                        "runnable" to runnable,
                        "errorNotifier" to errorNotifier
                    )
                )
            ).build()

        val trigger = TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
            .startNow()
            .build()

        scheduler.scheduleJob(job, trigger)

        if (!scheduler.isStarted) {
            scheduler.start()
        }
    }

    class RunnableJob : Job {
        override fun execute(context: JobExecutionContext) {
            try {
                val runnable = context.mergedJobDataMap.getValue("runnable") as Runnable
                runnable.run()
            } catch (e: Exception) {
                log.error("Job failed with exception $e")
                sendErrorNotification(context, e)
                scheduleRetry(context)
            }
        }

        private fun scheduleRetry(context: JobExecutionContext) {
            log.error("Scheduling retry job after 60 seconds")

            val trigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .startAt(Date.from(Instant.now().plusSeconds(60)))
                .forJob(context.jobDetail.key)
                .build()

            context.scheduler.scheduleJob(trigger)
        }

        private fun sendErrorNotification(context: JobExecutionContext, e: Exception) {
            val errorNotifier = context.mergedJobDataMap.getValue("errorNotifier") as Notifier
            errorNotifier.notify("Job failed with stacktrace: ${e.stackTrace}")
        }
    }
}
