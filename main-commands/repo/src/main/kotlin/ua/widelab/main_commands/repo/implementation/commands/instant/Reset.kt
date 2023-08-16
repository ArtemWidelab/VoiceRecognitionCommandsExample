package ua.widelab.main_commands.repo.implementation.commands.instant

import ua.widelab.main_commands.repo.implementation.CommandProvider
import ua.widelab.main_commands.repo.implementation.CommandStackInteractor
import ua.widelab.main_commands.repo.implementation.commands.Command
import ua.widelab.main_commands.repo.implementation.commands.Command.InstantCommand
import javax.inject.Inject

internal object Reset : InstantCommand {
    override fun invoke(commandStackInteractor: CommandStackInteractor) {
        if (commandStackInteractor.current() != null) {
            commandStackInteractor.removeLast()
        }
    }
}

internal class ResetCommandProvider @Inject constructor() : CommandProvider {
    override fun predicate(key: String, interactor: CommandStackInteractor): Boolean {
        return key == "reset" && interactor.current() != null
    }

    override fun create(): Command {
        return Reset
    }

}