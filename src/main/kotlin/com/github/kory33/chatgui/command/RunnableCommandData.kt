package com.github.kory33.chatgui.command

import com.github.kory33.chatgui.command.RunnableCommand.Companion.ASYNC_MODIFIER

/**
 * @author Kory
 */
data class RunnableCommandData(private val command: RunnableCommand,
                               val runnableId: Long,
                               val isAsync: Boolean) {
    val commandString = "${command.rootString} $runnableId" + if (isAsync) " $ASYNC_MODIFIER" else ""
}