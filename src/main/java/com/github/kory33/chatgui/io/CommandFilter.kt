package com.github.kory33.chatgui.io

import com.github.kory33.chatgui.model.player.IPlayerClickableChatInterface
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.core.filter.AbstractFilter
import org.apache.logging.log4j.message.Message
import java.util.regex.Pattern

/**
 * This class acts as a "filter" for runnable invocation commands.

 * When a player clicks on a button in [IPlayerClickableChatInterface],
 * a command `/signvote:signvote run` is run in order to invoke a registered runnable.

 * However, this can cause a lot of spam in the console. This class is useful in preventing such logs.
 */
class CommandFilter(command: String) : AbstractFilter(Filter.Result.DENY, Filter.Result.NEUTRAL), Filter {
    private val commandMatchPattern: Pattern

    init {
        val commandLogRegex = "^\\w{3,16} issued server command: $command.*"
        this.commandMatchPattern = Pattern.compile(commandLogRegex)

    }

    override fun filter(logger: Logger?, level: Level?, marker: Marker?, msg: String?,
                        vararg params: Any): Filter.Result {
        return filter(msg)
    }

    override fun filter(logger: Logger?, level: Level?, marker: Marker?, msg: Any?,
                        t: Throwable?): Filter.Result {
        if (msg == null) {
            return onMismatch
        }
        return filter(msg.toString())
    }

    override fun filter(logger: Logger?, level: Level?, marker: Marker?, msg: Message?,
                        t: Throwable?): Filter.Result {
        if (msg == null) {
            return onMismatch
        }
        val text = msg.formattedMessage
        return filter(text)
    }

    override fun filter(event: LogEvent?): Filter.Result {
        val text = event!!.message.formattedMessage
        return filter(text)
    }

    private fun filter(text: String?): Filter.Result {
        if (text == null) {
            return super.onMismatch
        }

        val matcher = this.commandMatchPattern.matcher(text)
        if (matcher.find()) {
            return super.onMatch
        }

        return super.onMismatch
    }
}
