package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class TitleValidationRuleTest {

    private lateinit var validator: TitleValidationRule

    @Before
    fun setup() {
        validator = TitleValidationRule()
    }

    @Test
    fun `validate accepts valid title`() {
        val task = Task(title = "Valid Task", description = "Valid description")
        
        // Should not throw any exception
        validator.validate(task)
    }

    @Test
    fun `validateTitle accepts valid title`() {
        val title = "Valid Task"
        
        // Should not throw any exception
        validator.validateTitle(title)
    }

    @Test
    fun `validateTitle rejects empty title`() {
        val title = ""
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateTitle(title)
        }
        assertEquals("Task title cannot be empty", exception.message)
    }

    @Test
    fun `validateTitle rejects blank title`() {
        val title = "   "
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateTitle(title)
        }
        assertEquals("Task title cannot be empty", exception.message)
    }

    @Test
    fun `validateTitle rejects title with leading whitespace`() {
        val title = " Title"
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateTitle(title)
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateTitle rejects title with trailing whitespace`() {
        val title = "Title "
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateTitle(title)
        }
        assertEquals("Task title cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateTitle accepts single character title`() {
        val title = "A"
        
        // Should not throw any exception
        validator.validateTitle(title)
    }

    @Test
    fun `validateTitle accepts maximum length title`() {
        val title = "A".repeat(100)
        
        // Should not throw any exception
        validator.validateTitle(title)
    }

    @Test
    fun `validateTitle rejects title exceeding maximum length`() {
        val title = "A".repeat(101)
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateTitle(title)
        }
        assertEquals("Task title cannot exceed 100 characters", exception.message)
    }
} 