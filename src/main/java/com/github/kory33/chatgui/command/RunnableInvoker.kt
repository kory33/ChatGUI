package com.github.kory33.chatgui.command

import com.github.kory33.chatgui.io.CommandFilter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
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
    : BukkitCommand(plugin.name.toLowerCase() + ":" + commandRoot) {

    private val runnableTable: HashMap<Long, () -> Unit> = HashMap()
    private val randomGenerator: Random = Random()
    private val scheduler: BukkitScheduler = plugin.server.scheduler

    private val rootCommandString: String
        get() = "/" + FALLBACK_PREFIX + ":" + this.name

    private fun getCommandString(runnableId: Long, async: Boolean): String {
        return this.rootCommandString + " " + runnableId + if (async) " " + ASYNC_MODIFIER else ""
    }

    /**
     * Get a command that is able to invoke the given runnable object
     * @param runnable target runnable object
     * *
     * @param isAsync specify whether or not the runnable should be invoked asynchronously
     * *
     * @return [RunnableCommand] object containing runnable id and command to cancel/invoke the runnable
     */
    fun registerRunnable(runnable: () -> Unit, isAsync: Boolean): RunnableCommand {
        while (true) {
            val runnableId = this.randomGenerator.nextLong()

            // re-generate id if the generated one is already registered
            if (this.runnableTable.containsKey(runnableId)) {
                continue
            }

            this.runnableTable.put(runnableId, runnable)
            return RunnableCommand(this.getCommandString(runnableId, isAsync), runnableId)
        }
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

        try {
            val runnableId = java.lang.Long.parseLong(args[0])
            val runnable = this.runnableTable[runnableId] ?: return true

            if (args.size > 1 && args[1].equals(ASYNC_MODIFIER, ignoreCase = true)) {
                this.scheduler.runTaskAsynchronously(this.plugin, runnable)
            } else {
                this.scheduler.runTask(this.plugin, runnable)
            }
        } catch (exception: NumberFormatException) {
            return true
        }

        return true
    }

    private fun addInvocationSuppressFilter() {
        if (invocationSuppressedPlugins.contains(this.plugin)) {
            return
        }
        (LogManager.getRootLogger() as Logger).addFilter(CommandFilter(this.rootCommandString))
        invocationSuppressedPlugins.add(this.plugin)
    }

    companion object {
        private val DEFAULT_COMMAND_ROOT = "run"
        private val ASYNC_MODIFIER = "async"
        private val FALLBACK_PREFIX = "runnableinvoker"

        private val invocationSuppressedPlugins = HashSet<JavaPlugin>()

        @JvmOverloads fun getRegisteredInstance(plugin: JavaPlugin, runCommandRoot: String = DEFAULT_COMMAND_ROOT, suppressCommand: Boolean = true): RunnableInvoker? {
            val commandExecutor = RunnableInvoker(plugin, runCommandRoot)

            try {
                val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
                bukkitCommandMap.isAccessible = true
                val commandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap
                commandMap.register(commandExecutor.name, FALLBACK_PREFIX, commandExecutor)

                if (suppressCommand) {
                    commandExecutor.addInvocationSuppressFilter()
                }

                return commandExecutor
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
                return null
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                return null
            }

        }
    }
}
