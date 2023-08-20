package ua.widelab.main_commands.repo.implementation.commands.instant

import ua.widelab.main_commands.repo.implementation.CommandProvider
import ua.widelab.main_commands.repo.implementation.CommandStackInteractor
import ua.widelab.main_commands.repo.implementation.commands.Command
import javax.inject.Inject

internal object Back : Command.InstantCommand {
    override fun invoke(commandStackInteractor: CommandStackInteractor) {
        commandStackInteractor.commit()
        commandStackInteractor.removeLast()
    }
}

internal class BackCommandProvider @Inject constructor() : CommandProvider {
    override fun predicate(key: String, interactor: CommandStackInteractor): Boolean {
        return key == "off"
    }

    override fun create(): Command {
        return Back
    }

}