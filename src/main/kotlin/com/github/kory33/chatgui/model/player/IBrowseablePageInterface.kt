package com.github.kory33.chatgui.model.player

import com.github.kory33.chatgui.manager.PlayerInteractiveInterfaceManager
import com.github.kory33.chatgui.tellraw.MessagePartsList
import com.github.ucchyocean.messaging.tellraw.MessageParts
import java.util.*

/**
 * A class representing an interface whose buttons at the bottom allow the player to move between pages.
 * @author kory
 */
interface IBrowseablePageInterface : IPlayerClickableChatInterface {
    val entryPerPage: Int
    val requestedPageIndex: Int
    val interfaceManager: PlayerInteractiveInterfaceManager

    /**
     * Get the list of all entries.
     * The list must be ordered in an appropriate way.
     * Each entry may not contain line ending at the end.
     * @return a list containing entries to be displayed on a page
     */
    val entryList: ArrayList<MessagePartsList>

    /**
     * Create new instance of browseable chat interface with the specified index.
     */
    fun yieldPage(pageIndex: Int): IBrowseablePageInterface

    /**
     * Get the line which is inserted before the table's main body
     * @return heading message
     */
    val heading: MessagePartsList

    /**
     * Get a string representing a button to go to the previous page
     * @param isActive boolean value true if and only if the button is active
     * *
     * @return button string
     */
    fun getPrevButton(isActive: Boolean): String

    /**
     * Get a string representing a button to go to the next page
     * @param isActive boolean value true if and only if the button is active
     * *
     * @return button string
     */
    fun getNextButton(isActive: Boolean): String

    fun getPageDisplayComponent(currentPageNumber: Int, maxPageNumber: Int): String

    /**
     * Get the body of the browse-able table.
     * @param finalPageIndex processed page index number(should not be out of range)
     * *
     * @return list representing the browse button interface component
     */
    private fun getBrowseButtonLine(finalPageIndex: Int, maximumPageIndex: Int): MessagePartsList {
        val messagePartsList = MessagePartsList()

        val prevButton = if (finalPageIndex != 0)
            this.getButton({
                val newInterface = this.yieldPage(finalPageIndex - 1)
                this.interfaceManager.registerInterface(newInterface)
                newInterface.send()
            }, this.getPrevButton(true))
        else
            MessageParts(this.getPrevButton(false))

        val nextButton = if (finalPageIndex != maximumPageIndex)
            this.getButton({
                val newInterface = this.yieldPage(finalPageIndex + 1)
                this.interfaceManager.registerInterface(newInterface)
                newInterface.send()
            }, this.getNextButton(true))
        else
            MessageParts(this.getNextButton(false))

        // add one to the displayed page numbers to make them start from 1
        val pageDisplay = MessageParts(this.getPageDisplayComponent(finalPageIndex + 1, maximumPageIndex + 1))

        messagePartsList.add(prevButton)
        messagePartsList.add(pageDisplay)
        messagePartsList.add(nextButton)

        return messagePartsList
    }

    /**
     * Get the body of the browseable table.
     * @param finalPageIndex processed page index number(should not be out of range)
     * *
     * @param entryList a list containing entries to be displayed.
     * *                  Entry order will be same as arranged in this list.
     */
    private fun getTableBody(finalPageIndex: Int, entryList: ArrayList<MessagePartsList>): MessagePartsList {
        val beginEntryIndex = this.entryPerPage * finalPageIndex
        val lastEntryIndex = Math.min(entryList.size, beginEntryIndex + this.entryPerPage)
        val displayList = entryList.subList(beginEntryIndex, lastEntryIndex)

        val messagePartsList = MessagePartsList()
        displayList.forEach({ messagePartsList.addLine(it) })

        return messagePartsList
    }

    override val bodyMessages: MessagePartsList
        get() {
            val entryList = this.entryList
            val maximumPageIndex = Math.floor(entryList.size * 1.0 / this.entryPerPage).toInt()
            val roundedPageIndex = Math.min(Math.max(0, this.requestedPageIndex), maximumPageIndex)

            val messagePartsList = MessagePartsList()
            messagePartsList.addAll(this.heading)
            messagePartsList.addAll(this.getTableBody(roundedPageIndex, entryList))
            messagePartsList.addAll(this.getBrowseButtonLine(roundedPageIndex, maximumPageIndex))

            return messagePartsList
        }
}
