package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.entity.TaskEntity
import amin.codelabs.qdo.data.task.repository.TaskRepositoryImpl
import amin.codelabs.qdo.domain.task.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class TaskRepositoryImplTest {

    private lateinit var mockDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        mockDao = mock()
        repository = TaskRepositoryImpl(mockDao)
    }

    @Test
    fun `addTask should insert task entity via dao`() = runTest {
        // Given
        val task = Task(id = 1, title = "Test", description = "Test", isDone = false)
        
        // When
        repository.addTask(task)
        
        // Then
        verify(mockDao).insertTask(any())
    }

    @Test
    fun `addTask should throw exception when dao throws exception`() = runTest {
        // Given
        val task = Task(id = 1, title = "Test", description = "Test", isDone = false)
        val exception = RuntimeException("Database error")

        whenever(mockDao.insertTask(any())).thenThrow(exception)
        // When & Then
        try {
            repository.addTask(task)
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }

    @Test
    fun `getTasks should return mapped tasks from dao`() = runTest {
        // Given
        val taskEntities = listOf(
            TaskEntity(
                id = 1,
                title = "Task 1",
                description = "Desc 1",
                isDone = false,
                createdAt = 1000L
            )
        )
        
        whenever(mockDao.getAllTasks()).thenReturn(flowOf(taskEntities))
        
        // When
        val result = repository.getTasks().first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Desc 1", result[0].description)
        assertEquals(false, result[0].isDone)
        assertEquals(1000L, result[0].createdAt)
    }
    
    @Test
    fun `getTasks should throw exception when dao throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        
        whenever(mockDao.getAllTasks()).thenThrow(exception)
        
        // When & Then
        try {
            repository.getTasks().first()
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }
    
    @Test
    fun `deleteTask should delete task by id via dao`() = runTest {
        // Given
        val taskId = 1L
        
        // When
        repository.deleteTask(taskId)
        
        // Then
        verify(mockDao).deleteTaskById(taskId)
    }
    
    @Test
    fun `deleteTask should throw exception when dao throws exception`() = runTest {
        // Given
        val taskId = 1L
        val exception = RuntimeException("Database error")
        
        doThrow(exception).whenever(mockDao).deleteTaskById(taskId)
        
        // When & Then
        try {
            repository.deleteTask(taskId)
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }
    
    @Test
    fun `updateTask should update task entity via dao`() = runTest {
        // Given
        val task = Task(id = 1, title = "Updated", description = "Updated", isDone = true)
        
        // When
        repository.updateTask(task)
        
        // Then
        verify(mockDao).updateTask(any())
    }
    
    @Test
    fun `updateTask should throw exception when dao throws exception`() = runTest {
        // Given
        val task = Task(id = 1, title = "Updated", description = "Updated", isDone = true)
        val exception = RuntimeException("Database error")
        
        doThrow(exception).whenever(mockDao).updateTask(any())
        
        // When & Then
        try {
            repository.updateTask(task)
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }
    
    @Test
    fun `getTaskById should return mapped task when task exists`() = runTest {
        // Given
        val taskEntity = TaskEntity(
            id = 1,
            title = "Test",
            description = "Test",
            isDone = false,
            createdAt = 1000L
        )
        
        whenever(mockDao.getTaskById(1L)).thenReturn(flowOf(taskEntity))
        
        // When
        val result = repository.getTaskById(1L).first()
        
        // Then
        assertEquals("Test", result?.title)
        assertEquals("Test", result?.description)
        assertEquals(false, result?.isDone)
        assertEquals(1000L, result?.createdAt)
    }
    
    @Test
    fun `getTaskById should return null when task does not exist`() = runTest {
        // Given
        whenever(mockDao.getTaskById(999L)).thenReturn(flowOf(null))
        
        // When
        val result = repository.getTaskById(999L).first()
        
        // Then
        assertNull(result)
    }
    
    @Test
    fun `getTaskById should throw exception when dao throws exception`() = runTest {
        // Given
        val taskId = 1L
        val exception = RuntimeException("Database error")
        
        whenever(mockDao.getTaskById(taskId)).thenThrow(exception)
        
        // When & Then
        try {
            repository.getTaskById(taskId).first()
            assertTrue("Expected exception was not thrown", false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }
    
    @Test
    fun `getTasks should return empty list when dao returns empty list`() = runTest {
        // Given
        whenever(mockDao.getAllTasks()).thenReturn(flowOf(emptyList()))
        
        // When
        val result = repository.getTasks().first()
        
        // Then
        assertEquals(0, result.size)
    }
    
    @Test
    fun `addTask should map domain task to entity correctly`() = runTest {
        // Given
        val task = Task(id = 1, title = "Test Task", description = "Test Description", isDone = true)
        var capturedEntity: TaskEntity? = null
        
        doAnswer { invocation ->
            capturedEntity = invocation.getArgument(0)
            Unit
        }.whenever(mockDao).insertTask(any())
        
        // When
        repository.addTask(task)
        
        // Then
        verify(mockDao).insertTask(any())
        assertEquals(task.id, capturedEntity?.id)
        assertEquals(task.title, capturedEntity?.title)
        assertEquals(task.description, capturedEntity?.description)
        assertEquals(task.isDone, capturedEntity?.isDone)
        assertEquals(task.createdAt, capturedEntity?.createdAt)
    }
    
    @Test
    fun `updateTask should map domain task to entity correctly`() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "Updated Task",
            description = "Updated Description",
            isDone = false
        )
        var capturedEntity: TaskEntity? = null
        
        doAnswer { invocation ->
            capturedEntity = invocation.getArgument(0)
            Unit
        }.whenever(mockDao).updateTask(any())
        
        // When
        repository.updateTask(task)
        
        // Then
        verify(mockDao).updateTask(any())
        assertEquals(task.id, capturedEntity?.id)
        assertEquals(task.title, capturedEntity?.title)
        assertEquals(task.description, capturedEntity?.description)
        assertEquals(task.isDone, capturedEntity?.isDone)
        assertEquals(task.createdAt, capturedEntity?.createdAt)
    }
    
    @Test
    fun `getTasks should handle multiple tasks correctly`() = runTest {
        // Given
        val taskEntities = listOf(
            TaskEntity(id = 1, title = "Task 1", description = "Desc 1", isDone = false, createdAt = 1000L),
            TaskEntity(id = 2, title = "Task 2", description = "Desc 2", isDone = true, createdAt = 2000L),
            TaskEntity(id = 3, title = "Task 3", description = "Desc 3", isDone = false, createdAt = 3000L)
        )
        
        whenever(mockDao.getAllTasks()).thenReturn(flowOf(taskEntities))
        
        // When
        val result = repository.getTasks().first()
        
        // Then
        assertEquals(3, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Task 2", result[1].title)
        assertEquals("Task 3", result[2].title)
        assertEquals(true, result[1].isDone)
        assertEquals(false, result[0].isDone)
        assertEquals(false, result[2].isDone)
    }
    
    @Test
    fun `getTaskById should handle null entity from dao`() = runTest {
        // Given
        whenever(mockDao.getTaskById(1L)).thenReturn(flowOf(null))
        
        // When
        val result = repository.getTaskById(1L).first()
        
        // Then
        assertNull(result)
    }
} 