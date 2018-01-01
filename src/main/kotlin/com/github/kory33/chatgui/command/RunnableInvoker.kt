package com.github.kory33.chatgui.command

import com.github.kory33.chatgui.util.bukkit.getCommandMap
import com.github.kory33.chatgui.util.io.CommandFilterManager
import com.github.kory33.chatgui.util.random.nextLongNotIn
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.util.*

/**
 * Class which handles registrations and invocations of runnable objects through Bukkit's command interface.
 * @author Kory
 */
class RunnableInvoker private constructor(private val plugin: JavaPlugin, commandRoot: String)
    : BukkitCommand("${plugin.name.toLowerCase()}:$commandRoot") {

    private val runnableTable: HashMap<Long, () -> Unit> = HashMap()
    private val randomGenerator: Random = Random()
    private val scheduler: BukkitScheduler = plugin.server.scheduler

    private val rootCommandString = "/$FALLBACK_PREFIX:${this.name}"

    private fun getCommandString(runnableId: Long, async: Boolean): String {
        return "${this.rootCommandString} $runnableId" + if (async) " $ASYNC_MODIFIER" else ""
    }

    /**
     * Get a command that is able to invoke the given runnable object
     * @param runnable target runnable object
     * @param isAsync specify whether or not the runnable should be invoked asynchronously
     * @return [RunnableCommandData] object containing runnable id and command to cancel/invoke the runnable
     */
    fun registerRunnable(runnable: () -> Unit, isAsync: Boolean): RunnableCommandData {
        val newRunnableId = this.randomGenerator.nextLongNotIn(this.runnableTable.keys)

        this.runnableTable.put(newRunnableId, runnable)
        return RunnableCommandData(this.getCommandString(newRunnableId, isAsync), newRunnableId)
    }

    /**
     * Remove and cancel a task associated with a given id.
     * @param runnableId id of the target runnable
     */
    fun cancelTask(runnableId: Long) {
        this.runnableTable.remove(runnableId)
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return true
        }

        val runnableId = args[0].toLongOrNull() ?: return true
        val runnable = this.runnableTable[runnableId] ?: return true

        if (args.size > 1 && args[1] == ASYNC_MODIFIER) {
            this.scheduler.runTaskAsynchronously(this.plugin, runnable)
        } else {
            this.scheduler.runTask(this.plugin, runnable)
        }

        return true
    }

    companion object {
        private const val DEFAULT_COMMAND_ROOT = "run"
        private const val ASYNC_MODIFIER = "async"
        private const val FALLBACK_PREFIX = "runnableinvoker"

        private val commandFilterManager = CommandFilterManager()

        @JvmOverloads fun getRegisteredInstance(plugin: JavaPlugin,
                                                runCommandRoot: String = DEFAULT_COMMAND_ROOT,
                                                suppressCommand: Boolean = true): RunnableInvoker? {
            return RunnableInvoker(plugin, runCommandRoot).also {
                plugin.server.getCommandMap().register(it.name, FALLBACK_PREFIX, it)

                if (suppressCommand) {
                    commandFilterManager.addFilterFor(it.rootCommandString)
                }
            }
        }
    }
}
