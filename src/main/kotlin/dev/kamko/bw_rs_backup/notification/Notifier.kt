package dev.kamko.bw_rs_backup.notification

interface Notifier {
    fun notify(message: String)
}
