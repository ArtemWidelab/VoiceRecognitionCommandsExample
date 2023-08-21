package ua.widelab.main_commands.repo.implementation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.from
import org.assertj.core.api.ListAssert
import org.assertj.core.api.ObjectAssert
import org.assertj.core.api.ThrowingConsumer
import org.junit.Rule
import org.junit.Test
import ua.widelab.main_commands.repo.implementation.commands.Command
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ListCommandStackInteractorTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `WHEN commands is empty AND add command to it EXPECT commands with one command`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput(""))

            //assert
            assertThat(resultCommands)
                .hasSize(2)
                .satisfiesExactly(
                    { item0 -> assertThat(item0).isEmpty() },
                    { item1 -> item1.assert(listOf("" to true)) }
                )
        }

    @Test
    fun `WHEN commands is empty AND add 3 commands to it EXPECT commands with 3 commands`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.addCommand(TestCommandWithInput("test2"))
            interactor.commit()
            interactor.addCommand(TestCommandWithInput("test3"))

            //assert
            assertThat(resultCommands)
                .hasSize(5)
                .satisfiesExactly(
                    { item0 -> assertThat(item0).isEmpty() },
                    { item1 ->
                        item1.assert(listOf("test1" to true))
                    },
                    { item2 ->
                        item2.assert(
                            listOf(
                                "test1" to false,
                                "test2" to true
                            )
                        )
                    },
                    { item3 ->
                        item3.assert(
                            listOf(
                                "test1" to false,
                                "test2" to false
                            )
                        )
                    },
                    { item4 ->
                        item4.assert(
                            listOf(
                                "test1" to false,
                                "test2" to false,
                                "test3" to true,
                            )
                        )
                    }
                )
        }

    @Test
    fun `WHEN commands is empty AND try to remove last EXPECT no changes`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            val removeResult = interactor.removeLast()

            //assert
            assertThat(removeResult)
                .isEqualTo(false)
            assertThat(resultCommands)
                .hasSize(1)
                .first()
                .asList()
                .hasSize(0)
        }

    @Test
    fun `WHEN commands is empty AND has current AND try to remove last EXPECT empty result`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            val removeResult = interactor.removeLast()

            //assert
            assertThat(removeResult)
                .isEqualTo(true)
            assertThat(resultCommands)
                .hasSize(3)
                .satisfiesExactly(
                    { item0 -> item0.assert(emptyList()) },
                    { item1 -> item1.assert(listOf("test1" to true)) },
                    { item2 -> item2.assert(emptyList()) },
                )
        }

    @Test
    fun `WHEN commands has 1 command AND not have current AND try to remove last EXPECT empty result`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.commit()
            val removeResult = interactor.removeLast()

            //assert
            assertThat(removeResult)
                .isEqualTo(true)
            assertThat(resultCommands)
                .hasSize(4)
                .satisfiesExactly(
                    { item0 -> item0.assert(emptyList()) },
                    { item1 -> item1.assert(listOf("test1" to true)) },
                    { item2 -> item2.assert(listOf("test1" to false)) },
                    { item3 -> item3.assert(emptyList()) },
                )
        }

    @Test
    fun `WHEN commands has 2 commands AND not have current AND try to remove last EXPECT 1 command`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.addCommand(TestCommandWithInput("test2"))
            interactor.commit()
            val removeResult = interactor.removeLast()

            //assert
            assertThat(removeResult)
                .isEqualTo(true)
            assertThat(resultCommands)
                .hasSize(5)
                .satisfiesExactly(
                    { item0 -> item0.assert(emptyList()) },
                    { item1 -> item1.assert(listOf("test1" to true)) },
                    { item2 -> item2.assert(listOf("test1" to false, "test2" to true)) },
                    { item3 -> item3.assert(listOf("test1" to false, "test2" to false)) },
                    { item4 -> item4.assert(listOf("test1" to false)) },
                )
        }

    @Test
    fun `WHEN commands has 1 command AND have current AND try to remove last EXPECT 1 command`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.addCommand(TestCommandWithInput("test2"))
            val removeResult = interactor.removeLast()

            //assert
            assertThat(removeResult)
                .isEqualTo(true)
            assertThat(resultCommands)
                .hasSize(4)
                .satisfiesExactly(
                    { item0 -> item0.assert(emptyList()) },
                    { item1 -> item1.assert(listOf("test1" to true)) },
                    { item2 -> item2.assert(listOf("test1" to false, "test2" to true)) },
                    { item3 -> item3.assert(listOf("test1" to false)) },
                )
        }

    @Test
    fun `WHEN commands is empty AND have current AND try to get current EXPECT current`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            val current = interactor.current()

            //assert
            assertThat(current)
                .isNotNull
                .isInstanceOfSatisfying(TestCommandWithInput::class.java) {
                    assertThat(it.data).isEqualTo("test1")
                }
        }

    @Test
    fun `WHEN commands is not empty AND have current AND try to get current EXPECT current`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.addCommand(TestCommandWithInput("test2"))
            val current = interactor.current()

            //assert
            assertThat(current)
                .isNotNull
                .isInstanceOfSatisfying(TestCommandWithInput::class.java) {
                    assertThat(it.data).isEqualTo("test2")
                }
        }

    @Test
    fun `WHEN commands is not empty AND not have current AND try to get current EXPECT null`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput("test1"))
            interactor.addCommand(TestCommandWithInput("test2"))
            interactor.commit()
            val current = interactor.current()

            //assert
            assertThat(current).isNull()
        }

    @Test
    fun `WHEN commands is empty AND not have current AND try to get current EXPECT null`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            val current = interactor.current()

            //assert
            assertThat(current).isNull()
        }

    @Test
    fun `WHEN not have current AND try to send new data EXPECT false`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            val acceptResult = interactor.accept("test")

            //assert
            assertThat(acceptResult).isFalse()
        }

    @Test
    fun `WHEN have current AND try to send invalid data EXPECT false`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }


            //act
            interactor.addCommand(TestCommandWithInput(""))
            val acceptResult = interactor.accept("test")

            //assert
            assertThat(acceptResult).isFalse()
        }

    @Test
    fun `WHEN have current AND try to send valid data EXPECT new changed value`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput(""))
            val acceptResult = interactor.accept("1")

            //assert
            assertThat(acceptResult).isTrue()
            assertThat(resultCommands)
                .hasSize(3)
                .satisfiesExactly(
                    { item0 -> item0.assert(emptyList()) },
                    { item1 -> item1.assert(listOf("" to true)) },
                    { item2 -> item2.assert(listOf("1" to true)) },
                )
        }

    @Test
    fun `WHEN have current AND try to commit EXPECT no current`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.addCommand(TestCommandWithInput(""))
            val current1 = interactor.current()
            interactor.commit()
            val current2 = interactor.current()

            //assert
            assertThat(current1).isNotNull()
            assertThat(current2).isNull()
        }

    @Test
    fun `WHEN no current AND try to commit EXPECT nothing happens`() =
        runTest {
            //arrange
            val interactor = ListCommandStackInteractor()

            val resultCommands = mutableListOf<List<CommandStackInteractor.CommandWrapper>>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                interactor.all.toList(resultCommands)
            }

            //act
            interactor.commit()

            //assert
            assertThat(resultCommands)
                .hasSize(1)
                .first()
                .asList()
                .hasSize(0)
        }
}

private fun ObjectAssert<CommandStackInteractor.CommandWrapper>.assertWrapper(
    expectedString: String,
    expectedCurrent: Boolean
): AbstractObjectAssert<*, *>? {
    return this
        .returns(expectedCurrent, from { it.isCurrent })
        .extracting("command")
        .isInstanceOfSatisfying(TestCommandWithInput::class.java) {
            assertThat(it.data).isEqualTo(expectedString)
        }
}

private fun List<CommandStackInteractor.CommandWrapper>.assert(data: List<Pair<String, Boolean>>): ListAssert<CommandStackInteractor.CommandWrapper> {
    fun mapper(pair: Pair<String, Boolean>): ThrowingConsumer<CommandStackInteractor.CommandWrapper> {
        return ThrowingConsumer<CommandStackInteractor.CommandWrapper> { input ->
            assertThat(input)
                .assertWrapper(
                    expectedString = pair.first,
                    expectedCurrent = pair.second
                )
        }
    }

    return assertThat(this)
        .hasSize(data.size)
        .satisfiesExactly(*data.map { mapper(it) }.toTypedArray())
}

data class TestCommandWithInput(
    val data: String,
    override val id: String = UUID.randomUUID().toString()
) : Command.CommandWithInput {
    override fun accept(value: String): TestCommandWithInput? {
        if (value.matches("\\d".toRegex())) {
            return TestCommandWithInput(
                this.data + value
            )
        }
        return null
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun getFormattedValue(): String {
        TODO("Not yet implemented")
    }

}