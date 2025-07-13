package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DescriptionValidationRuleTest {

    private lateinit var validator: DescriptionValidationRule

    @Before
    fun setup() {
        validator = DescriptionValidationRule()
    }

    @Test
    fun `validate accepts valid description`() {
        val task = Task(title = "Valid Task", description = "Valid description")
        
        // Should not throw any exception
        validator.validate(task)
    }

    @Test
    fun `validate accepts task with null description`() {
        val task = Task(title = "Valid Task", description = null)
        
        // Should not throw any exception
        validator.validate(task)
    }

    @Test
    fun `validate accepts task with empty description`() {
        val task = Task(title = "Valid Task", description = "")
        
        // Should not throw any exception
        validator.validate(task)
    }

    @Test
    fun `validateDescription accepts valid description`() {
        val description = "Valid description"
        
        // Should not throw any exception
        validator.validateDescription(description)
    }

    @Test
    fun `validateDescription accepts null description`() {
        val description: String? = null
        
        // Should not throw any exception
        validator.validateDescription(description)
    }

    @Test
    fun `validateDescription accepts empty description`() {
        val description = ""
        
        // Should not throw any exception
        validator.validateDescription(description)
    }

    @Test
    fun `validateDescription accepts maximum length description`() {
        val description = "A".repeat(500)
        
        // Should not throw any exception
        validator.validateDescription(description)
    }

    @Test
    fun `validateDescription rejects description exceeding maximum length`() {
        val description = "A".repeat(501)
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateDescription(description)
        }
        assertEquals("Task description cannot exceed 500 characters", exception.message)
    }

    @Test
    fun `validateDescription rejects description with leading whitespace`() {
        val description = " Description"
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateDescription(description)
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateDescription rejects description with trailing whitespace`() {
        val description = "Description "
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validateDescription(description)
        }
        assertEquals("Task description cannot start or end with whitespace", exception.message)
    }

    @Test
    fun `validateDescription accepts description with internal whitespace`() {
        val description = "Description with internal spaces"
        
        // Should not throw any exception
        validator.validateDescription(description)
    }
} 