package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskDatabaseException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExperimentalCoroutinesApi
class GetTasksUseCaseTest {

    @Mock
    lateinit var repository: TaskRepository

    private lateinit var useCase: GetTasksUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetTasksUseCase(repository)
    }

    @Test
    fun `invoke returns empty list when repository returns empty list`() = runTest {
        whenever(repository.getTasks()).thenReturn(flowOf(emptyList()))

        useCase().test {
            val tasks = awaitItem()
            assertEquals(0, tasks.size)
            awaitComplete()
        }
        verify(repository).getTasks()
    }

    @Test
    fun `invoke returns tasks when repository returns tasks`() = runTest {
        val expectedTasks = listOf(
            Task(id = 1L, title = "Task 1", description = "Description 1", isDone = false),
            Task(id = 2L, title = "Task 2", description = "Description 2", isDone = true)
        )
        whenever(repository.getTasks()).thenReturn(flowOf(expectedTasks))

        useCase().test {
            val tasks = awaitItem()
            assertEquals(2, tasks.size)
            assertEquals("Task 1", tasks[0].title)
            assertEquals("Task 2", tasks[1].title)
            assertEquals(false, tasks[0].isDone)
            assertEquals(true, tasks[1].isDone)
            awaitComplete()
        }
        verify(repository).getTasks()
    }

    @Test
    fun `invoke propagates database exceptions from repository`() = runTest {
        val dbException = TaskDatabaseException("Database error", IOException("disk full"))
        whenever(repository.getTasks()).thenReturn(flow { throw dbException })

        useCase().test {
            val error = awaitError()
            assertTrue(error is TaskDatabaseException)
            assertEquals("Database error", error.message)
        }
        verify(repository).getTasks()
    }

    @Test
    fun `invoke propagates other repository exceptions`() = runTest {
        val repoException = amin.codelabs.qdo.domain.task.TaskUnknownException("Unknown error")
        whenever(repository.getTasks()).thenReturn(flow { throw repoException })

        useCase().test {
            val error = awaitError()
            assertTrue(error is amin.codelabs.qdo.domain.task.TaskUnknownException)
            assertEquals("Unknown error", error.message)
        }
        verify(repository).getTasks()
    }

    @Test
    fun `invoke handles multiple emissions from repository`() = runTest {
        val firstEmission = listOf(Task(id = 1L, title = "First", description = "First task"))
        val secondEmission = listOf(
            Task(id = 1L, title = "First", description = "First task"),
            Task(id = 2L, title = "Second", description = "Second task")
        )
        
        whenever(repository.getTasks()).thenReturn(flowOf(firstEmission, secondEmission))

        useCase().test {
            val firstTasks = awaitItem()
            assertEquals(1, firstTasks.size)
            assertEquals("First", firstTasks[0].title)

            val secondTasks = awaitItem()
            assertEquals(2, secondTasks.size)
            assertEquals("First", secondTasks[0].title)
            assertEquals("Second", secondTasks[1].title)

            awaitComplete()
        }
        verify(repository).getTasks()
    }
} 