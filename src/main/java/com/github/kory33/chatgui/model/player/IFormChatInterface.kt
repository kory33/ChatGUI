package com.github.kory33.chatgui.model.player

import com.github.kory33.chatgui.listener.PlayerChatInterceptor
import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.ucchyocean.messaging.tellraw.MessageComponent
import com.github.ucchyocean.messaging.tellraw.MessageParts
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * A class representing a player chat interface which is capable of accepting
 * player's input through its input forms.
 * @author kory
 */
interface IFormChatInterface : IPlayerClickableChatInterface {
    var inputCancelButton: MessageParts?
    val chatInterceptor: PlayerChatInterceptor

    /**
     * Nullify the session.
     * Clear all associated runnables from cache table
     * and mark the session as invalid.
     */
    override fun revokeSession() {
        super.revokeSession()
        this.chatInterceptor.cancelAnyInterception(this.targetPlayer, "UI session has been revoked.")
        this.inputCancelButton = null
    }

    private // send footer
            // notify cancellation
            // re-send the interface
    val cancelInputButton: MessageParts
        get() {
            this.inputCancelButton = super.getButton({
                this.chatInterceptor.cancelAnyInterception(this.targetPlayer, "Input cancelled.")
                super.revokeButton(this.inputCancelButton!!)
                MessageComponent(this.interfaceFooter).send(this.targetPlayer)
                this.notifyInputCancellation()
                this.send()
            }, this.getInputCancelButton())
            return this.inputCancelButton!!
        }

    private fun promptInput(fieldName: String) {
        val message = this.getFieldInputPromptMessage(fieldName)
        val cancelInputButton = this.cancelInputButton

        val messagePartsList = MessagePartsList()
        messagePartsList.add(message)
        messagePartsList.add(" ")
        messagePartsList.add(cancelInputButton)

        MessageComponent(messagePartsList).send(this.targetPlayer)
    }

    private fun getInputToForm(onPlayerSendString: Consumer<String>, validator: Predicate<String>) {
        chatInterceptor.interceptFirstMessageFrom(this.targetPlayer)
                .thenAccept { input ->
                    if (!validator.test(input)) {
                        this.notifyInvalidInput()
                        this.getInputToForm(onPlayerSendString, validator)
                        return@thenAccept
                    }
                    onPlayerSendString.accept(input)
                    super.revokeButton(this.inputCancelButton!!)
                    this.send()
                }
                .exceptionally { null }
    }

    /**
     * Notify the player that the input value is invalid,
     * asking the input to be attempted again.
     */
    fun notifyInvalidInput()

    /**
     * Get a string that is used as a button to edit the value in the form.
     * @return a string that represents an edit-button
     */
    val editButtonString: String

    /**
     * Get a label component string
     * @param labelName name of the form field
     * *
     * @return a formatted string that gets displayed as a label of a field.
     */
    fun getLabelString(labelName: String): String

    /**
     * Get a value component string.
     *
     *
     * Concrete implementation of this method may color
     * the value or even return as it is given as an argument.
     *
     *
     * When `value` argument is null,
     * this method should return a string indicating that no value is set to the field.
     * @param value value of the form field.
     * *
     * @return a formatted string that gets displayed as a value of a field.
     * * "Not set" state should be indicated when null.
     */
    fun getValueString(value: String): String

    /**
     * Notify the player that the field input prompt has been cancelled.
     */
    fun notifyInputCancellation()

    /**
     * Get a string that represents a button to cancel the input prompt
     * @return a button string
     */
    fun getInputCancelButton(): String

    /**
     * Get a message that prompts the player to input a value to the field.
     * @param fieldName name of the field.
     * *
     * @return message that prompts the player to input a value to the field.
     */
    fun getFieldInputPromptMessage(fieldName: String): String

    /**
     * Get a list representing a form to which the target player can input string data.
     * @param onPlayerSendString action which is invoked after the target player inputs a validated string.
     * *
     * @param validator predicate which determines if a given input string is valid.
     * *                  When this method returns true, player's input is passed to `onPlayerSendString` consumer.
     * *
     * @param label string that labels this form
     * *
     * @param value string that gets displayed as current value.
     * *
     * @return a list of message parts representing an input form
     */
    fun getForm(onPlayerSendString: Consumer<String>, validator: Predicate<String>, label: String, value: String): MessagePartsList {
        val formName = MessageParts(this.getLabelString(label))

        val formValue = MessageParts(this.getValueString(value))

        val editButton = this.getButton({
            this.revokeAllRunnables()
            this.promptInput(label)
            this.getInputToForm(onPlayerSendString, validator)
        }, this.editButtonString)

        val form = MessagePartsList()

        form.add(formName)
        form.add(formValue)
        form.add(editButton)
        form.addLine("")

        return form
    }
}
