package com.github.kory33.chatgui.command

import com.github.kory33.chatgui.util.io.CommandFilterManager
import com.github.kory33.chatgui.util.random.nextLongNotIn
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * Class which handles registrations and invocations of runnable objects through Bukkit's command interface.
 * @author Kory
 */
class RunnableInvoker private constructor(private val plugin: JavaPlugin, commandRoot: String) {
    private val runnableTable = HashMap<Long, () -> Unit>()
    private val randomGenerator = Random()
    private val scheduler = plugin.server.scheduler

    private val command = RunnableCommand(this, plugin, commandRoot)

    /**
     * Get a command that is able to invoke the given runnable object
     * @param runnable target runnable object
     * @param isAsync specify whether or not the runnable should be invoked asynchronously
     * @return [RunnableCommandData] object containing runnable id and command to cancel/invoke the runnable
     */
    fun registerRunnable(runnable: () -> Unit, isAsync: Boolean): RunnableCommandData {
        val newRunnableId = this.randomGenerator.nextLongNotIn(this.runnableTable.keys)

        this.runnableTable.put(newRunnableId, runnable)
        return RunnableCommandData(command, newRunnableId, isAsync)
    }

    /**
     * Removes a task associated with a given id.
     * @param runnableId id of the target runnable
     */
    fun removeRunnable(runnableId: Long) = this.runnableTable.remove(runnableId)

    /**
     * Execute the task synchronously or asynchronously using a scheduler provided from Bukkit.
     */
    fun execute(async: Boolean, task: () -> Unit): BukkitTask = if (async) {
        this.scheduler.runTaskAsynchronously(this.plugin, task)
    } else {
        this.scheduler.runTask(this.plugin, task)
    }

    companion object {
        private const val DEFAULT_COMMAND_ROOT = "run"

        private val commandFilterManager = CommandFilterManager()

        @JvmOverloads fun getRegisteredInstance(plugin: JavaPlugin,
                                                runCommandRoot: String = DEFAULT_COMMAND_ROOT,
                                                suppressCommand: Boolean = true): RunnableInvoker? {
            return RunnableInvoker(plugin, runCommandRoot).also {
                if (suppressCommand) {
                    commandFilterManager.addFilterFor(it.command.rootString)
                }
            }
        }
    }
}
