package ua.widelab.main_commands.repo.implementation.commands.input

import androidx.core.text.isDigitsOnly
import ua.widelab.main_commands.repo.implementation.CommandProvider
import ua.widelab.main_commands.repo.implementation.CommandStackInteractor
import ua.widelab.main_commands.repo.implementation.commands.Command
import java.util.UUID
import javax.inject.Inject

data class Count(
    val value: String = "",
    override val id: String = UUID.randomUUID().toString()
) : Command.CommandWithInput {
    override fun accept(value: String): Count? {
        if (value.isDigitsOnly()) {
            return this.copy(value = this.value + value)
        }
        return null
    }

    override fun getTitle(): String {
        return "Count"
    }

    override fun getFormattedValue(): String {
        return value
    }
}

internal class CountCommandProvider @Inject constructor() : CommandProvider {
    override fun predicate(key: String, interactor: CommandStackInteractor): Boolean {
        return key == "follow"
    }

    override fun create(): Command {
        return Count()
    }

}