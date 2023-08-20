package ua.widelab.main_commands.repo.implementation.commands

import ua.widelab.main_commands.repo.implementation.CommandStackInteractor

internal sealed interface Command {
    interface CommandWithInput : Command {
        val id: String
        fun accept(value: String): CommandWithInput?
        fun getTitle(): String
        fun getFormattedValue(): String
    }

    fun interface InstantCommand : Command {
        fun invoke(commandStackInteractor: CommandStackInteractor)
    }
}