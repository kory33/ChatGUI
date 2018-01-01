package com.github.kory33.chatgui.command

import com.github.kory33.chatgui.util.bukkit.getCommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.java.JavaPlugin

class RunnableCommand (private val runnableInvoker: RunnableInvoker,
                       plugin: JavaPlugin,
                       commandRoot: String)
    : BukkitCommand("${plugin.name.toLowerCase()}:$commandRoot") {

    init {
        plugin.server.getCommandMap().register(this.name, FALLBACK_PREFIX, this)
    }

    val rootString = "/$FALLBACK_PREFIX:${this.name}"

    override fun execute(sender: CommandSender?, commandLabel: String?, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            return true
        }

        val runnableId = args[0].toLongOrNull() ?: return true
        val runnable = runnableInvoker.removeRunnable(runnableId) ?: return true

        val executeAsync = args.size > 1 && args[1] == ASYNC_MODIFIER
        runnableInvoker.execute(executeAsync, runnable)

        return true
    }

    companion object {
        const val ASYNC_MODIFIER = "async"
        const val FALLBACK_PREFIX = "runnableinvoker"
    }
}