package amin.codelabs.qdo.domain.task.usecase

import amin.codelabs.qdo.domain.task.TaskValidationException
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

class DeleteTaskUseCaseTest {

    @Mock
    lateinit var repository: TaskRepository

    private lateinit var useCase: DeleteTaskUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository when task ID is valid`() = runTest {
        val taskId = 1L

        useCase(taskId)

        verify(repository).deleteTask(taskId)
    }

    @Test
    fun `invoke throws TaskValidationException when task ID is zero`() {
        val taskId = 0L

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(taskId) }
        }
        assertEquals("Task ID must be greater than 0", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when task ID is negative`() {
        val taskId = -1L

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(taskId) }
        }
        assertEquals("Task ID must be greater than 0", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke throws TaskValidationException when task ID is very negative`() {
        val taskId = Long.MIN_VALUE

        val exception = assertThrows(TaskValidationException::class.java) {
            runTest { useCase(taskId) }
        }
        assertEquals("Task ID must be greater than 0", exception.message)
        verifyNoInteractions(repository)
    }

    @Test
    fun `invoke accepts positive task ID`() = runTest {
        val taskId = 42L

        useCase(taskId)

        verify(repository).deleteTask(taskId)
    }

    @Test
    fun `invoke accepts maximum positive task ID`() = runTest {
        val taskId = Long.MAX_VALUE

        useCase(taskId)

        verify(repository).deleteTask(taskId)
    }

} 