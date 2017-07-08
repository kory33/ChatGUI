package com.github.kory33.chatgui.model.player

import com.github.kory33.chatgui.model.IChatInterface
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Represents an abstract player chat interface object
 * @author Kory
 */
interface IPlayerChatInterface : IChatInterface {
    val targetPlayer: Player

    fun send() {
        super.send(this.targetPlayer)
    }


    @Deprecated("""Use {@link IPlayerChatInterface#send()} instead.
        Any argument passed to this method WILL be ignored and
        {@link IPlayerChatInterface#send()} is then run.""", ReplaceWith("this.send()"))
    override fun send(target: CommandSender) {
        this.send()
    }
}
