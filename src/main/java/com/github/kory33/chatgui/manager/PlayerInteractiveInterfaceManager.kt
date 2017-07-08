package com.github.kory33.chatgui.manager

import org.bukkit.entity.Player

import com.github.kory33.chatgui.model.player.IPlayerClickableChatInterface

/**
 * A class which holds the association of player onto a single session.
 * No player can have two valid sessions registered in this manager class.
 * @author kory
 */
class PlayerInteractiveInterfaceManager {
    private val playerInterfaceMap = HashMap<Player, IPlayerClickableChatInterface>()

    /**
     * Register an interface session.
     * Any interface that is already associated with the target player
     * will be revoked and replaced
     * @param interactiveInterface interface to be bound to a player.
     */
    fun registerInterface(interactiveInterface: IPlayerClickableChatInterface) {
        val targetPlayer = interactiveInterface.targetPlayer
        val registeredInterface = this.playerInterfaceMap[targetPlayer]
        registeredInterface?.revokeSession()
        this.playerInterfaceMap.put(targetPlayer, interactiveInterface)
    }
}
