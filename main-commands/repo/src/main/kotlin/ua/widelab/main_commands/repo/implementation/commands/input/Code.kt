package ua.widelab.main_commands.repo.implementation.commands.input

import androidx.core.text.isDigitsOnly
import ua.widelab.main_commands.repo.implementation.CommandProvider
import ua.widelab.main_commands.repo.implementation.CommandStackInteractor
import ua.widelab.main_commands.repo.implementation.commands.Command
import javax.inject.Inject

internal data class Code(
    val data: String = ""
) : Command.CommandWithInput {
    override fun accept(value: String): Command.CommandWithInput? {
        if (value.isDigitsOnly()) {
            return Code(
                data = this.data + value
            )
        }
        return null
    }

    override fun getTitle(): String {
        return "Code"
    }

    override fun getFormattedValue(): String {
        return data
    }
}

internal class CodeCommandProvider @Inject constructor() : CommandProvider {
    override fun predicate(key: String, interactor: CommandStackInteractor): Boolean {
        return key == "go"
    }

    override fun create(): Command {
        return Code()
    }

}

