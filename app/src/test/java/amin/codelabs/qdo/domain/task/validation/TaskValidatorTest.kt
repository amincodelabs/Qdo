package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TaskValidatorTest {

    @Test
    fun `validateForCreation accepts valid task`() {
        val task = Task(title = "Valid Task", description = "Valid description")
        
        // Should not throw any exception
        TaskValidator.validateForCreation(task)
    }

    @Test
    fun `validateForCreation rejects empty title`() {
        val task = Task(title = "", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task title cannot be empty", exception.message)
    }

    @Test
    fun `validateForCreation rejects blank title`() {
        val task = Task(title = "   ", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task title cannot be empty", exception.message)
    }

    @Test
    fun `validateForCreation rejects title with leading whitespace`() {
        val task = Task(title = " Title", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateForCreation rejects title with trailing whitespace`() {
        val task = Task(title = "Title ", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateForCreation accepts null description`() {
        val task = Task(title = "Valid Task", description = null)
        
        // Should not throw any exception
        TaskValidator.validateForCreation(task)
    }

    @Test
    fun `validateForCreation accepts empty description`() {
        val task = Task(title = "Valid Task", description = "")
        
        // Should not throw any exception
        TaskValidator.validateForCreation(task)
    }

    @Test
    fun `validateForCreation rejects description with leading whitespace`() {
        val task = Task(title = "Valid Task", description = " Description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateForCreation rejects description with trailing whitespace`() {
        val task = Task(title = "Valid Task", description = "Description ")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForCreation(task)
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateForUpdate accepts valid task with valid ID`() {
        val task = Task(id = 1L, title = "Valid Task", description = "Valid description")
        
        // Should not throw any exception
        TaskValidator.validateForUpdate(task)
    }

    @Test
    fun `validateForUpdate rejects task with zero ID`() {
        val task = Task(id = 0L, title = "Valid Task", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForUpdate(task)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validateForUpdate rejects task with negative ID`() {
        val task = Task(id = -1L, title = "Valid Task", description = "Valid description")
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForUpdate(task)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    // Deletion validation tests
    @Test
    fun `validateForDeletion accepts positive task ID`() {
        val taskId = 1L
        
        // Should not throw any exception
        TaskValidator.validateForDeletion(taskId)
    }

    @Test
    fun `validateForDeletion accepts maximum positive task ID`() {
        val taskId = Long.MAX_VALUE
        
        // Should not throw any exception
        TaskValidator.validateForDeletion(taskId)
    }

    @Test
    fun `validateForDeletion rejects zero task ID`() {
        val taskId = 0L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForDeletion(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validateForDeletion rejects negative task ID`() {
        val taskId = -1L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForDeletion(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validateForDeletion rejects minimum negative task ID`() {
        val taskId = Long.MIN_VALUE
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForDeletion(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    // Retrieval validation tests
    @Test
    fun `validateForRetrieval accepts positive task ID`() {
        val taskId = 1L
        
        // Should not throw any exception
        TaskValidator.validateForRetrieval(taskId)
    }

    @Test
    fun `validateForRetrieval accepts maximum positive task ID`() {
        val taskId = Long.MAX_VALUE
        
        // Should not throw any exception
        TaskValidator.validateForRetrieval(taskId)
    }

    @Test
    fun `validateForRetrieval rejects zero task ID`() {
        val taskId = 0L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForRetrieval(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validateForRetrieval rejects negative task ID`() {
        val taskId = -1L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForRetrieval(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validateForRetrieval rejects minimum negative task ID`() {
        val taskId = Long.MIN_VALUE
        
        val exception = assertThrows(TaskValidationException::class.java) {
            TaskValidator.validateForRetrieval(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }
} 