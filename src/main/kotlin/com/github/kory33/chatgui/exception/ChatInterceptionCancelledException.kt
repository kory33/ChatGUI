package com.github.kory33.chatgui.exception

/**
 * Represents an exception thrown when a player chat interception is cancelled
 */
data class ChatInterceptionCancelledException(val reason: String) : Exception()
