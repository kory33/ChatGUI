package com.github.kory33.chatgui.util.io

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Logger

/**
 * A manager class used for adding a logger-filter of a command.
 */
class CommandFilterManager {
    private val filteredCommands = HashSet<String>()

    /**
     * Adds a filter for a given [commandString].
     * This function does nothing if the [commandString] has been already filtered.
     */
    fun addFilterFor(commandString: String) {
        if (filteredCommands.contains(commandString)) {
            return
        }

        val logger = LogManager.getRootLogger() as Logger
        logger.addFilter(CommandFilter(commandString))
    }
}