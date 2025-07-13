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

class AddTaskUseCaseTest {

    @Mock
    lateinit var repository: TaskRepository

    private lateinit var useCase: AddTaskUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = AddTaskUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository when task is valid`() = runTest {
        val task = Task(title = "Valid Task", description = "Valid description")

        useCase(task)

        verify(repository).addTask(task)
    }

    @Test
    fun `invoke throws TaskValidationException when title is empty`() {
        val task = Task(title = "", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot be empty", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title is blank`() {
        val task = Task(title = "   ", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot be empty", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title has leading whitespace`() {
        val task = Task(title = " Title", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when title has trailing whitespace`() {
        val task = Task(title = "Title ", description = "Valid description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when description has leading whitespace`() {
        val task = Task(title = "Valid Task", description = " Description")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when description has trailing whitespace`() {
        val task = Task(title = "Valid Task", description = "Description ")

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(task) }
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke accepts task with null description`() = runTest {
        val task = Task(title = "Valid Task", description = null)

        useCase(task)

        verify(repository).addTask(task)
    }

    @Test
    fun `invoke accepts task with empty description`() = runTest {
        val task = Task(title = "Valid Task", description = "")

        useCase(task)

        verify(repository).addTask(task)
    }
} 