package amin.codelabs.qdo.domain.task.validation

import amin.codelabs.qdo.domain.task.TaskValidationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class TaskIdValidationRuleTest {

    private lateinit var validator: TaskIdValidationRule

    @Before
    fun setup() {
        validator = DefaultTaskIdValidationRule()
    }

    @Test
    fun `validate accepts positive task ID`() {
        val taskId = 1L
        
        // Should not throw any exception
        validator.validate(taskId)
    }

    @Test
    fun `validate accepts maximum positive task ID`() {
        val taskId = Long.MAX_VALUE
        
        // Should not throw any exception
        validator.validate(taskId)
    }

    @Test
    fun `validate accepts large positive task ID`() {
        val taskId = 999999999L
        
        // Should not throw any exception
        validator.validate(taskId)
    }

    @Test
    fun `validate rejects zero task ID`() {
        val taskId = 0L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validate(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validate rejects negative task ID`() {
        val taskId = -1L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validate(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validate rejects minimum negative task ID`() {
        val taskId = Long.MIN_VALUE
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validate(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }

    @Test
    fun `validate rejects large negative task ID`() {
        val taskId = -999999999L
        
        val exception = assertThrows(TaskValidationException::class.java) {
            validator.validate(taskId)
        }
        assertEquals("Task ID must be greater than 0", exception.message)
    }
} 