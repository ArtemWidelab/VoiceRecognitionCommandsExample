package ua.widelab.main_commands.repo.implementation.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ua.widelab.main_commands.repo.CommandProcessor
import ua.widelab.main_commands.repo.implementation.CommandProcessorImpl
import ua.widelab.main_commands.repo.implementation.CommandProvider
import ua.widelab.main_commands.repo.implementation.CommandStackInteractor
import ua.widelab.main_commands.repo.implementation.ListCommandStackInteractor
import ua.widelab.main_commands.repo.implementation.commands.input.CodeCommandProvider
import ua.widelab.main_commands.repo.implementation.commands.input.CountCommandProvider
import ua.widelab.main_commands.repo.implementation.commands.instant.BackCommandProvider
import ua.widelab.main_commands.repo.implementation.commands.instant.ResetCommandProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class CommandsModule {
    @Binds
    @IntoSet
    internal abstract fun bindCodeCommandProvider(
        codeCommandProvider: CodeCommandProvider
    ): CommandProvider

    @Binds
    @IntoSet
    internal abstract fun bindCountCommandProvider(
        countCommandProvider: CountCommandProvider
    ): CommandProvider

    @Binds
    @IntoSet
    internal abstract fun bindBackCommandProvider(
        backCommandProcessor: BackCommandProvider
    ): CommandProvider

    @Binds
    @IntoSet
    internal abstract fun bindResetCommandProvider(
        resetCommandProvider: ResetCommandProvider
    ): CommandProvider

    @Binds
    internal abstract fun bindCommandsStackInteractor(
        commandStackInteractor: ListCommandStackInteractor
    ): CommandStackInteractor

    @Binds
    internal abstract fun bindCommandProcessor(
        commandProcessor: CommandProcessorImpl
    ): CommandProcessor
}