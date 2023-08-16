package ua.widelab.main_commands.repo

import kotlinx.coroutines.flow.Flow
import ua.widelab.main_commands.entities.CommandResult

interface CommandProcessor {

    /**
     * Pushes a word into the processing queue.
     *
     * This function is used to send a word for processing
     *
     * @param word The word to be pushed for processing.
     * @return `true` if the word is accepted as valid input, `false` otherwise.
     */
    fun pushWord(word: String): Boolean

    val result: Flow<List<CommandResult>>
}