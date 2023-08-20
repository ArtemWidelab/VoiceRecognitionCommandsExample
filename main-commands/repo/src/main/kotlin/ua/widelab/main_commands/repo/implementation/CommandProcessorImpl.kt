package ua.widelab.main_commands.repo.implementation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.widelab.main_commands.entities.CommandResult
import ua.widelab.main_commands.repo.CommandProcessor
import ua.widelab.main_commands.repo.implementation.commands.Command
import javax.inject.Inject

internal class CommandProcessorImpl @Inject constructor(
    private val commandStackInteractor: CommandStackInteractor,
    private val commandFactory: CommandFactory
) : CommandProcessor {

    private fun normalizeWord(word: String): String {
        return when (val w = word.trim().lowercase()) {
            "zero" -> "0"
            "one" -> "1"
            "two" -> "2"
            "three" -> "3"
            "four" -> "4"
            "five" -> "5"
            "six" -> "6"
            "seven" -> "7"
            "eight" -> "8"
            "nine" -> "9"
            else -> w
        }
    }

    override fun pushWord(word: String): Boolean {
        val normalizedWord = normalizeWord(word)
        if (commandStackInteractor.accept(normalizedWord)) return true
        val newCommand =
            commandFactory.createCommand(normalizedWord, commandStackInteractor) ?: return false

        when (newCommand) {
            is Command.CommandWithInput -> {
                commandStackInteractor.addCommand(newCommand)
            }

            is Command.InstantCommand -> {
                newCommand.invoke(commandStackInteractor)
            }
        }
        return true
    }

    override val result: Flow<List<CommandResult>> = commandStackInteractor.all.map {
        it.map {
            CommandResult(
                name = it.command.getTitle(),
                value = it.command.getFormattedValue(),
                isCurrent = it.isCurrent,
                id = it.command.id
            )
        }
    }
}