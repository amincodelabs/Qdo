package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.domain.task.repository.TaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class UpdateTaskUseCaseTest {

    @Mock
    lateinit var repository: TaskRepository

    private lateinit var useCase: UpdateTaskUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository when task is valid`() = runTest {
        val task = Task(id = 1L, title = "Valid Task", description = "Valid description")

        useCase(task)

        verify(repository).updateTask(task)
    }

    @Test
    fun `invoke throws TaskValidationException when task ID is zero`() {
        val task = Task(id = 0L, title = "Valid Task", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task ID must be greater than 0", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when task ID is negative`() {
        val task = Task(id = -1L, title = "Valid Task", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task ID must be greater than 0", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title is empty`() {
        val task = Task(id = 1L, title = "", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot be empty", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title is blank`() {
        val task = Task(id = 1L, title = "   ", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot be empty", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title has leading whitespace`() {
        val task = Task(id = 1L, title = " Title", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title has trailing whitespace`() {
        val task = Task(id = 1L, title = "Title ", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when description has leading whitespace`() {
        val task = Task(id = 1L, title = "Valid Task", description = " Description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when description has trailing whitespace`() {
        val task = Task(id = 1L, title = "Valid Task", description = "Description ")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke accepts task with null description`() {
        val task = Task(id = 1L, title = "Valid Task", description = null)

        runTest {
            useCase(task)
            verify(repository).updateTask(task)
        }
    }

    @Test
    fun `invoke accepts task with empty description`() = runTest {
        val task = Task(id = 1L, title = "Valid Task", description = "")

        useCase(task)

        verify(repository).updateTask(task)
    }

    @Test
    fun `invoke accepts task with valid ID and data`() = runTest {
        val task = Task(
            id = 42L,
            title = "Updated Task",
            description = "Updated description",
            isDone = true
        )

        useCase(task)

        verify(repository).updateTask(task)
    }

} 