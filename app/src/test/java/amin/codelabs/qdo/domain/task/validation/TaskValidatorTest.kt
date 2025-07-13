package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TaskValidatorTest {

    // --- Creation ---
    @Test
    fun `validateForCreation accepts valid task`() {
        val task = Task(title = "Valid Task", description = "Valid description")
        // Should not throw
        TaskValidator.validateForCreation(task)
    }

    @Test
    fun `validateForCreation throws for invalid title`() {
        val task = Task(title = "", description = "Valid description")
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task title cannot be empty", ex.message)
    }

    @Test
    fun `validateForCreation throws for invalid description`() {
        val task = Task(title = "Valid Task", description = " Description")
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task description cannot start or end with whitespace", ex.message)
    }

    // --- Update ---
    @Test
    fun `validateForUpdate accepts valid task`() {
        val task = Task(id = 1L, title = "Valid Task", description = "Valid description")
        // Should not throw
        TaskValidator.validateForUpdate(task)
    }

    @Test
    fun `validateForUpdate throws for invalid id`() {
        val task = Task(id = 0L, title = "Valid Task", description = "Valid description")
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForUpdate(task)
        }
        assertEquals("Task ID must be greater than 0", ex.message)
    }

    @Test
    fun `validateForUpdate throws for invalid title`() {
        val task = Task(id = 1L, title = "", description = "Valid description")
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForUpdate(task)
        }
        assertEquals("Task title cannot be empty", ex.message)
    }

    @Test
    fun `validateForUpdate throws for invalid description`() {
        val task = Task(id = 1L, title = "Valid Task", description = " Description")
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForUpdate(task)
        }
        assertEquals("Task description cannot start or end with whitespace", ex.message)
    }

    // --- Deletion ---
    @Test
    fun `validateForDeletion accepts valid id`() {
        TaskValidator.validateForDeletion(1L) // Should not throw
    }

    @Test
    fun `validateForDeletion throws for invalid id`() {
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForDeletion(0L)
        }
        assertEquals("Task ID must be greater than 0", ex.message)
    }

    // --- Retrieval ---
    @Test
    fun `validateForRetrieval accepts valid id`() {
        TaskValidator.validateForRetrieval(1L) // Should not throw
    }

    @Test
    fun `validateForRetrieval throws for invalid id`() {
        val ex = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForRetrieval(-1L)
        }
        assertEquals("Task ID must be greater than 0", ex.message)
    }
} 