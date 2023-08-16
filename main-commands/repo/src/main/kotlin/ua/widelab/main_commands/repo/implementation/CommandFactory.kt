package ua.widelab.main_commands.repo.implementation

import ua.widelab.main_commands.repo.implementation.commands.Command
import javax.inject.Inject

internal interface CommandProvider {
    fun predicate(key: String, interactor: CommandStackInteractor): Boolean
    fun create(): Command
}

internal class CommandFactory @Inject constructor(
    private val commandProviders: Set<@JvmSuppressWildcards CommandProvider>
) {
    fun createCommand(key: String, interactor: CommandStackInteractor): Command? {
        val normalizedKey = key.lowercase()
        return commandProviders.firstOrNull { it.predicate(normalizedKey, interactor) }?.create()
    }
}