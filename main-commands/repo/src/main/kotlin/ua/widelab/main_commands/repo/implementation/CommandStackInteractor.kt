package ua.widelab.main_commands.repo.implementation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ua.widelab.main_commands.repo.implementation.commands.Command
import javax.inject.Inject

internal interface CommandStackInteractor {
    fun addCommand(newCommand: Command.CommandWithInput)
    fun removeLast(): Boolean
    fun current(): Command.CommandWithInput?
    fun accept(input: String): Boolean
    fun commit()

    val all: Flow<List<CommandWrapper>>

    data class CommandWrapper(
        val command: Command.CommandWithInput,
        val isCurrent: Boolean
    )
}

internal class ListCommandStackInteractor @Inject constructor() : CommandStackInteractor {

    private data class InteractorState(
        val commands: PersistentList<Command.CommandWithInput> = persistentListOf<Command.CommandWithInput>(),
        val current: Command.CommandWithInput? = null
    ) {
        fun isEmpty(): Boolean {
            return current == null && commands.isEmpty()
        }
    }

    private val state = MutableStateFlow(InteractorState())

    override fun addCommand(newCommand: Command.CommandWithInput) {
        state.update {
            it.copy(
                commands = it.commands.addNotNull(it.current),
                current = newCommand
            )
        }
    }

    override fun removeLast(): Boolean {
        state.update {
            if (it.isEmpty()) return false
            it.copy(
                current = null,
                commands = if (it.current == null) it.commands.removeAt(it.commands.lastIndex) else it.commands
            )
        }
        return true
    }

    override fun current(): Command.CommandWithInput? {
        return state.value.current
    }

    override fun accept(input: String): Boolean {
        val newCommand = current()?.accept(input) ?: return false
        state.update {
            it.copy(
                current = newCommand
            )
        }
        return true
    }

    override fun commit() {
        state.update {
            it.copy(
                current = null,
                commands = it.commands.addNotNull(it.current)
            )
        }
    }

    override val all: Flow<List<CommandStackInteractor.CommandWrapper>> = state
        .map {
            (it.commands.map { CommandStackInteractor.CommandWrapper(it, false) } + listOfNotNull(
                it.current?.let { CommandStackInteractor.CommandWrapper(it, true) }
            )).toPersistentList()
        }
}

private fun <E> PersistentList<E>.addNotNull(element: E?): PersistentList<E> {
    if (element == null) return this
    return this.add(element)
}
