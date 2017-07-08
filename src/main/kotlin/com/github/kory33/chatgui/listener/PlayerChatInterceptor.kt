package com.github.kory33.chatgui.listener

import java.util.HashMap
import java.util.concurrent.CompletableFuture

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

import com.github.kory33.chatgui.exception.ChatInterceptionCancelledException

/**
 * A Bukkit listener class which can be used to intercept a player message.
 */
class PlayerChatInterceptor(hostPlugin: JavaPlugin) : Listener {
    private val interceptionFutureMap: MutableMap<Player, CompletableFuture<String>>

    init {
        hostPlugin.server.pluginManager.registerEvents(this, hostPlugin)
        this.interceptionFutureMap = HashMap<Player, CompletableFuture<String>>()
    }

    /**
     * Attempts to complete interception future when a player sends a chat message.
     */
    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val sender = event.player

        val future = this.interceptionFutureMap.remove(sender) ?: return

        future.complete(event.message)
        event.isCancelled = true
    }

    /**
     * Cancels chat interception when the player has quit.
     */
    @EventHandler
    fun onPlayerExit(event: PlayerQuitEvent) {
        this.cancelAnyInterception(event.player, "Player has quit.")
    }

    /**
     * Cancel chat interception task associated with a given player, if there exists any.
     * @param player target player whose chat message interception is cancelled
     * *
     * @param cancelReason reason for cancellation(optional)
     */
    fun cancelAnyInterception(player: Player, cancelReason: String) {
        val future = this.interceptionFutureMap.remove(player) ?: return

        future.completeExceptionally(ChatInterceptionCancelledException(cancelReason))
    }

    /**
     * Intercept the first message sent by the given player.
     * @param sourcePlayer target player whose first chat message is to be intercepted
     * *
     * @return a completable future which completes with a first message from the player
     */
    fun interceptFirstMessageFrom(sourcePlayer: Player): CompletableFuture<String> {
        val future = CompletableFuture<String>()

        // cancel previous interception
        this.cancelAnyInterception(sourcePlayer, "New interception scheduled.")

        this.interceptionFutureMap.put(sourcePlayer, future)
        return future
    }
}
