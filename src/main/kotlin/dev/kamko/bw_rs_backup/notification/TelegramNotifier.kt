package dev.kamko.bw_rs_backup.notification

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(TelegramNotifier::class.java)

data class TelegramConfig(val botToken: String, val chatId: String)

class TelegramNotifier(
    private val config: TelegramConfig
) : Notifier {

    private val bot = TelegramBot(config.botToken)

    override fun publish(message: String) {
        log.debug("Sending telegram message to chatId=${config.chatId}, message=$message")
        bot.execute(SendMessage(config.chatId, message))
    }
}
