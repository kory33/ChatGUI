package com.github.kory33.chatgui.util.bukkit

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap

fun Server.getCommandMap(): CommandMap {
    val bukkitCommandMapField = javaClass.getDeclaredField("commandMap")
    bukkitCommandMapField.isAccessible = true

    return bukkitCommandMapField.get(Bukkit.getServer()) as CommandMap
}