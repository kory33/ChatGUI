package com.github.kory33.chatgui.tellraw

import com.github.ucchyocean.messaging.tellraw.MessageParts

/**
 * A class representing an ArrayList of MessageParts
 * @author kory
 */
class MessagePartsList() : ArrayList<MessageParts>() {
    /**
     * Construct list containing a given message part.
     * @param messageParts a message part to be contained in the list
     */
    constructor(messageParts: MessageParts) : this() {
        this.add(messageParts)
    }

    /**
     * Construct list containing a message part which has given message string as a body.
     * @param messageString a string to be contained in the list
     */
    constructor(messageString: String) : this() {
        this.add(messageString)
    }

    /**
     * Append a MessageParts and a line break at the end of this list.
     * @param messageParts a message to be added along with a line break
     */
    fun addLine(messageParts: MessageParts) {
        this.add(messageParts)
        this.add("\n")
    }

    /**
     * Append a MessageParts and a line break at the end of this list.
     * @param messageString a string to be added along with a line ending
     */
    fun addLine(messageString: String) {
        this.addLine(MessageParts(messageString))
    }

    /**
     * Append an array of MessageParts and a line break at the end of the collection
     * @param collection a collection of messages to be inserted into the list
     */
    fun addLine(collection: Collection<MessageParts>) {
        this.addAll(collection)
        this.addLine("")
    }

    /**
     * Append a string element at the end of this list
     * @param message a message to be added
     */
    fun add(message: String) {
        this.add(MessageParts(message))
    }
}
