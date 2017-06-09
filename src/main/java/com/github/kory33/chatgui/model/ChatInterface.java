package com.github.kory33.chatgui.model;

import org.bukkit.command.CommandSender;

import com.github.kory33.chatgui.tellraw.MessagePartsList;
import com.github.ucchyocean.messaging.tellraw.MessageComponent;

/**
 * An abstract class which represents the chat UI.
 * An implementation of this class should have
 * no reference to the player whilst constructing the UI.
 * @author kory33
 */
public abstract class ChatInterface {
    protected abstract MessagePartsList constructInterfaceMessages();

    /**
     * Construct the user interface and send to a CommandSender.
     * @param target to whom this interface should be sent
     */
    public void send(CommandSender target) {
        MessageComponent messageComponent = new MessageComponent(this.constructInterfaceMessages());
        messageComponent.send(target);
    }
}