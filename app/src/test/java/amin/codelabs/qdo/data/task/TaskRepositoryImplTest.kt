package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.data.task.dao.TaskDao
import amin.codelabs.qdo.data.task.entity.TaskEntity
import amin.codelabs.qdo.data.task.mapper.TaskEntityMapper
import amin.codelabs.qdo.data.task.repository.TaskRepositoryImpl
import amin.codelabs.qdo.domain.task.TaskDatabaseException
import amin.codelabs.qdo.domain.task.TaskNotFoundException
import amin.codelabs.qdo.domain.task.TaskUnknownException
import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.model.Task
import amin.codelabs.qdo.infrastructure.logger.Logger
import app.cash.turbine.test
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.io.IOException
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class TaskRepositoryImplTest {

    @Mock
    lateinit var dao: TaskDao
    @Mock
    lateinit var logger: Logger
    private lateinit var repo: TaskRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repo = TaskRepositoryImpl(dao, logger)
    }

    @Test
    fun `addTask delegates to dao on success`() = runTest {
        val task = Task(42, "T", "D", false)

        repo.addTask(task)

        verify(dao).insertTask(TaskEntityMapper.toEntity(task))
        verifyNoInteractions(logger)
    }

    @Test
    fun `addTask maps IOException to TaskDatabaseException`() = runTest {
        val io = IOException("disk full")
        whenever(dao.insertTask(any())).thenAnswer { throw io }

        val ex = try {
            repo.addTask(Task(1, "", "", false))
            null
        } catch (e: TaskDatabaseException) {
            e
        }

        assertNotNull("Expected TaskDatabaseException", ex)
        assertEquals("Database error", ex!!.message)
        verify(logger).logError(io, "TaskRepositoryImpl.addTask error: IOException: disk full")
    }

    @Test
    fun `addTask rethrows CancellationException without logging`() = runTest {
        val cancel = CancellationException("stop")
        whenever(dao.insertTask(any())).thenAnswer { throw cancel }

        val thrown = try {
            repo.addTask(Task(1, "", "", false))
            null
        } catch (e: CancellationException) {
            e
        }
        assertSame("Expected same CancellationException", cancel, thrown)
        verifyNoInteractions(logger)
    }

    @Test
    fun `addTask maps IllegalArgumentException to TaskValidationException`() = runTest {
        val ill = IllegalArgumentException("bad arg")
        whenever(dao.insertTask(any())).thenAnswer { throw ill }

        val ex = try {
            repo.addTask(Task(1, "", "", false))
            null
        } catch (e: TaskValidationException) {
            e
        }

        assertNotNull("Expected TaskValidationException", ex)
        assertEquals("bad arg", ex!!.message)
        verify(logger).logError(
            ill,
            "TaskRepositoryImpl.addTask error: IllegalArgumentException: bad arg"
        )
    }

    @Test
    fun `addTask maps unknown Exception to TaskUnknownException`() = runTest {
        val ru = RuntimeException("uh oh")
        whenever(dao.insertTask(any())).thenAnswer { throw ru }

        val ex = try {
            repo.addTask(Task(1, "", "", false))
            null
        } catch (e: TaskUnknownException) {
            e
        }

        assertNotNull("Expected TaskUnknownException", ex)
        assertEquals("uh oh", ex!!.message)
        verify(logger).logError(ru, "TaskRepositoryImpl.addTask error: RuntimeException: uh oh")
    }

    // — getTasks —

    @Test
    fun `getTasks emits mapped Task list on success`() = runTest {
        val entities = listOf(
            TaskEntity(1, "A", "a", false),
            TaskEntity(2, "B", "b", true)
        )
        whenever(dao.getAllTasks()).thenReturn(flowOf(entities))

        repo.getTasks().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("A", items[0].title)
            assertEquals("B", items[1].title)
            awaitComplete()
        }
        verifyNoInteractions(logger)
    }

    @Test
    fun `getTasks maps IOException in flow to TaskDatabaseException`() = runTest {
        whenever(dao.getAllTasks()).thenReturn(flow { throw IOException("fail") })

        repo.getTasks().test {
            val err = awaitError()
            assertTrue(err is TaskDatabaseException)
            assertEquals("Database error", err.message)
        }
        verify(logger).logError(any(), eq("TaskRepositoryImpl.getTasks error: IOException: fail"))
    }

    // — deleteTask —

    @Test
    fun `deleteTask delegates on success`() = runTest {
        repo.deleteTask(99L)

        verify(dao).deleteTaskById(99L)
        verifyNoInteractions(logger)
    }

    @Test
    fun `deleteTask maps TimeoutException to TaskDatabaseException`() = runTest {
        val te = TimeoutException("too slow")
        whenever(dao.deleteTaskById(5L)).thenAnswer { throw te }

        val ex = try {
            repo.deleteTask(5L)
            null
        } catch (e: TaskDatabaseException) {
            e
        }

        assertNotNull("Expected TaskDatabaseException", ex)
        assertEquals("Database error", ex!!.message)
        verify(logger).logError(
            te,
            "TaskRepositoryImpl.deleteTask error: TimeoutException: too slow"
        )
    }

    // — updateTask —

    @Test
    fun `updateTask delegates on success`() = runTest {
        val t = Task(7, "Z", "z", true)

        repo.updateTask(t)

        verify(dao).updateTask(TaskEntityMapper.toEntity(t))
        verifyNoInteractions(logger)
    }

    @Test
    fun `getTaskById emits Task when entity present`() = runTest {
        val entity = TaskEntity(3, "X", "x", false)
        whenever(dao.getTaskById(3L)).thenReturn(flowOf(entity))

        repo.getTaskById(3L).test {
            assertEquals("X", awaitItem()!!.title)
            awaitComplete()
        }
        verifyNoInteractions(logger)
    }

    @Test
    fun `getTaskById maps other exceptions to TaskUnknownException`() = runTest {
        val ru = RuntimeException("bad")
        whenever(dao.getTaskById(1L)).thenReturn(flow { throw ru })

        repo.getTaskById(1L).test {
            val err = awaitError()
            assertTrue(err is TaskUnknownException)
            assertEquals("bad", err.message)
        }
        verify(logger).logError(ru, "TaskRepositoryImpl.getTaskById error: RuntimeException: bad")
    }
}



