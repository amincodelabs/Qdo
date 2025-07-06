package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.data.task.entity.TaskEntity
import amin.codelabs.qdo.data.task.mapper.TaskEntityMapper
import amin.codelabs.qdo.domain.task.model.Task
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class TaskEntityMapperTest {

    @Test
    fun `fromEntity should map TaskEntity to Task correctly`() {
        // Given
        val entity = TaskEntity(
            id = 1L,
            title = "Test Task",
            description = "Test Description",
            isDone = true,
            createdAt = 1234567890L
        )

        // When
        val result = TaskEntityMapper.fromEntity(entity)

        // Then
        assertNotNull(result)
        assertEquals(1L, result.id)
        assertEquals("Test Task", result.title)
        assertEquals("Test Description", result.description)
        assertEquals(true, result.isDone)
        assertEquals(1234567890L, result.createdAt)
    }

    @Test
    fun `toEntity should map Task to TaskEntity correctly`() {
        // Given
        val task = Task(
            id = 2L,
            title = "Another Task",
            description = "Another Description",
            isDone = false,
            createdAt = 9876543210L
        )

        // When
        val result = TaskEntityMapper.toEntity(task)

        // Then
        assertNotNull(result)
        assertEquals(2L, result.id)
        assertEquals("Another Task", result.title)
        assertEquals("Another Description", result.description)
        assertEquals(false, result.isDone)
        assertEquals(9876543210L, result.createdAt)
    }

    @Test
    fun `fromEntity should handle task with empty description`() {
        // Given
        val entity = TaskEntity(
            id = 3L,
            title = "Task with empty description",
            description = "",
            isDone = false,
            createdAt = 1111111111L
        )

        // When
        val result = TaskEntityMapper.fromEntity(entity)

        // Then
        assertEquals("", result.description)
        assertEquals("Task with empty description", result.title)
        assertEquals(false, result.isDone)
    }

    @Test
    fun `toEntity should handle task with empty description`() {
        // Given
        val task = Task(
            id = 4L,
            title = "Task with empty description",
            description = "",
            isDone = true,
            createdAt = 2222222222L
        )

        // When
        val result = TaskEntityMapper.toEntity(task)

        // Then
        assertEquals("", result.description)
        assertEquals("Task with empty description", result.title)
        assertEquals(true, result.isDone)
    }

    @Test
    fun `fromEntity should handle task with null description`() {
        // Given
        val entity = TaskEntity(
            id = 5L,
            title = "Task with null description",
            description = null,
            isDone = false,
            createdAt = 3333333333L
        )

        // When
        val result = TaskEntityMapper.fromEntity(entity)

        // Then
        assertEquals(null, result.description)
        assertEquals("Task with null description", result.title)
        assertEquals(false, result.isDone)
    }

    @Test
    fun `toEntity should handle task with null description`() {
        // Given
        val task = Task(
            id = 6L,
            title = "Task with null description",
            description = null,
            isDone = true,
            createdAt = 4444444444L
        )

        // When
        val result = TaskEntityMapper.toEntity(task)

        // Then
        assertEquals(null, result.description)
        assertEquals("Task with null description", result.title)
        assertEquals(true, result.isDone)
    }

    @Test
    fun `fromEntity should handle task with zero id`() {
        // Given
        val entity = TaskEntity(
            id = 0L,
            title = "Task with zero id",
            description = "Test description",
            isDone = false,
            createdAt = 5555555555L
        )

        // When
        val result = TaskEntityMapper.fromEntity(entity)

        // Then
        assertEquals(0L, result.id)
        assertEquals("Task with zero id", result.title)
        assertEquals("Test description", result.description)
        assertEquals(false, result.isDone)
    }

    @Test
    fun `toEntity should handle task with zero id`() {
        // Given
        val task = Task(
            id = 0L,
            title = "Task with zero id",
            description = "Test description",
            isDone = true,
            createdAt = 6666666666L
        )

        // When
        val result = TaskEntityMapper.toEntity(task)

        // Then
        assertEquals(0L, result.id)
        assertEquals("Task with zero id", result.title)
        assertEquals("Test description", result.description)
        assertEquals(true, result.isDone)
    }

    @Test
    fun `fromEntity should handle task with long title`() {
        // Given
        val longTitle = "This is a very long task title that might contain many characters and should be handled properly by the mapper without any issues"
        val entity = TaskEntity(
            id = 7L,
            title = longTitle,
            description = "Test description",
            isDone = false,
            createdAt = 7777777777L
        )

        // When
        val result = TaskEntityMapper.fromEntity(entity)

        // Then
        assertEquals(longTitle, result.title)
        assertEquals(7L, result.id)
        assertEquals("Test description", result.description)
        assertEquals(false, result.isDone)
    }

    @Test
    fun `toEntity should handle task with long title`() {
        // Given
        val longTitle = "This is a very long task title that might contain many characters and should be handled properly by the mapper without any issues"
        val task = Task(
            id = 8L,
            title = longTitle,
            description = "Test description",
            isDone = true,
            createdAt = 8888888888L
        )

        // When
        val result = TaskEntityMapper.toEntity(task)

        // Then
        assertEquals(longTitle, result.title)
        assertEquals(8L, result.id)
        assertEquals("Test description", result.description)
        assertEquals(true, result.isDone)
    }

    @Test
    fun `roundtrip mapping should preserve all values`() {
        // Given
        val originalTask = Task(
            id = 999L,
            title = "Original Task",
            description = "Original Description",
            isDone = true,
            createdAt = 9999999999L
        )

        // When
        val entity = TaskEntityMapper.toEntity(originalTask)
        val mappedBackTask = TaskEntityMapper.fromEntity(entity)

        // Then
        assertEquals(originalTask.id, mappedBackTask.id)
        assertEquals(originalTask.title, mappedBackTask.title)
        assertEquals(originalTask.description, mappedBackTask.description)
        assertEquals(originalTask.isDone, mappedBackTask.isDone)
        assertEquals(originalTask.createdAt, mappedBackTask.createdAt)
    }

    @Test
    fun `roundtrip mapping should preserve all values for entity to task to entity`() {
        // Given
        val originalEntity = TaskEntity(
            id = 888L,
            title = "Original Entity",
            description = "Original Entity Description",
            isDone = false,
            createdAt = 8888888888L
        )

        // When
        val task = TaskEntityMapper.fromEntity(originalEntity)
        val mappedBackEntity = TaskEntityMapper.toEntity(task)

        // Then
        assertEquals(originalEntity.id, mappedBackEntity.id)
        assertEquals(originalEntity.title, mappedBackEntity.title)
        assertEquals(originalEntity.description, mappedBackEntity.description)
        assertEquals(originalEntity.isDone, mappedBackEntity.isDone)
        assertEquals(originalEntity.createdAt, mappedBackEntity.createdAt)
    }
} 