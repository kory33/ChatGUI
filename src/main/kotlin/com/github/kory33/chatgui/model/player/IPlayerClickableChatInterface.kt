package com.github.kory33.chatgui.model.player

import com.github.kory33.chatgui.command.RunnableInvoker
import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.kory33.chatgui.util.collection.BijectiveHashMap
import com.github.kory33.chatgui.util.collection.*
import com.github.ucchyocean.messaging.tellraw.ClickEventType
import com.github.ucchyocean.messaging.tellraw.HoverParts
import com.github.ucchyocean.messaging.tellraw.MessageParts

/**
 * A class representing a player chat interface which is capable of navigating the player
 * through clicks on the buttons
 * @author kory
 */
interface IPlayerClickableChatInterface : IPlayerChatInterface {
    var isValidSession: Boolean
        set

    val buttonIdMapping: BijectiveHashMap<MessageParts, Long>
    val runnableInvoker: RunnableInvoker

    /**
     * Revoke all runnables bound to this interface.
     */
    fun revokeAllRunnables() {
        // remove all the bound runnable objects
        this.buttonIdMapping.values.forEach({ this.runnableInvoker.cancelTask(it) })
        this.buttonIdMapping.clear()
    }

    /**
     * Revoke a particular button
     */
    fun revokeButton(buttonMessagePart: MessageParts) {
        val runnableId = this.buttonIdMapping[buttonMessagePart] ?: return

        this.runnableInvoker.cancelTask(runnableId)
        this.buttonIdMapping.removeKey(buttonMessagePart)
    }

    /**
     * Nullify the session.
     * Clear all associated runnables from cache table
     * and mark the session as invalid.
     */
    fun revokeSession() {
        this.isValidSession = false
        this.revokeAllRunnables()
    }

    /**
     * Get a message component which invokes the given runnable object when clicked.
     * @param runnable runnable to be run(synchronously) when the player clicks the button
     * @param buttonString message that gets displayed as the button
     * @return a button message component that is bound to the runnable object
     */
    fun getButton(runnable: () -> Unit, buttonString: String): MessageParts {
        return getButton(runnable, buttonString, null)
    }

    /**
     * Get a message component which invokes the given runnable object when clicked.
     * @param runnable runnable to be run(synchronously) when the player clicks the button
     * @param buttonString message that gets displayed as the button
     * @param toolTipString string that gets displayed on mouse hover
     * @return a button message component that is bound to the runnable object
     */
    fun getButton(runnable: () -> Unit, buttonString: String, toolTipString: String?): MessageParts {
        val button = MessageParts(buttonString)
        val command = this.runnableInvoker.registerRunnable(runnable, false)
        button.setClickEvent(ClickEventType.RUN_COMMAND, command.commandString)
        this.buttonIdMapping.put(button, command.runnableId)

        if (toolTipString != null) {
            button.setHoverParts(HoverParts(toolTipString))
        }

        return button
    }

    /**
     * Cancel the action aimed by the interface and revoke the interface object
     */
    fun cancelAction(cancelMessage: String) {
        if (!this.isValidSession) {
            return
        }

        this.revokeSession()
        this.targetPlayer.sendMessage(cancelMessage)
    }

    /**
     * Get the body of the chat interface.
     * Returned body is enclosed by a header and a footer and is finally sent to the player.
     * @return a list of messages that represents a body of the interface
     */
    val bodyMessages: MessagePartsList

    /**
     * Construct the interface.
     *
     * By default, the content of this interface will be equal to the return value of [bodyMessages]
     *
     * Override this method if the interface should be in other form.
     */
    override fun constructInterfaceMessages(): MessagePartsList {
        return this.bodyMessages
    }
}
