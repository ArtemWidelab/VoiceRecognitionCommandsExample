package ua.widelab.main_commands.entities

data class CommandResult(
    val id: String,
    val name: String,
    val value: String,
    val isCurrent: Boolean
)