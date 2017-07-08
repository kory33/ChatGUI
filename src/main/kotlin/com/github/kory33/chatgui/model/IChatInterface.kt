package com.github.kory33.chatgui.model

import org.bukkit.command.CommandSender

import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.ucchyocean.messaging.tellraw.MessageComponent

/**
 * An abstract class which represents the chat UI.
 * An implementation of this class should have
 * no reference to the player whilst constructing the UI.
 * @author kory33
 */
interface IChatInterface {
    fun constructInterfaceMessages(): MessagePartsList

    /**
     * Construct the user interface and send to a CommandSender.
     * @param target to whom this interface should be sent
     */
    fun send(target: CommandSender) {
        val messageComponent = MessageComponent(this.constructInterfaceMessages())
        messageComponent.send(target)
    }
}
