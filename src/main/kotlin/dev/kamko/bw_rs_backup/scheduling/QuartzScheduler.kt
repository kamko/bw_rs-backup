package dev.kamko.bw_rs_backup.scheduling

import org.quartz.*
import org.quartz.impl.StdSchedulerFactory


class QuartzScheduler {

    private val sf = StdSchedulerFactory()
    private val scheduler = sf.getScheduler()

    fun scheduleJob(runnable: Runnable, cron: String) {
        val job = JobBuilder.newJob(RunnableJob::class.java)
            .setJobData(
                JobDataMap(
                    mapOf(
                        "runnable" to runnable
                    )
                )
            ).build();

        val trigger = TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(cron))
            .startNow()
            .build()

        scheduler.scheduleJob(
            job, trigger
        )

        if (!scheduler.isStarted) {
            scheduler.start()
        }
    }

    class RunnableJob : Job {
        override fun execute(context: JobExecutionContext?) {
            (context!!.mergedJobDataMap.getValue("runnable") as Runnable).run()
        }
    }

}
