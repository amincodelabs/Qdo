package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.domain.task.TaskDatabaseException
import amin.codelabs.qdo.domain.task.TaskNotFoundException
import amin.codelabs.qdo.domain.task.TaskUnknownException
import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.infrastructure.logger.Logger
import android.database.sqlite.SQLiteAccessPermException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteFullException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

class TaskRepositoryErrorHandlerTest {
    private val logger: Logger = mock()

    @Test
    fun `maps SQLiteConstraintException to TaskDatabaseException and logs`() {
        val ex = SQLiteConstraintException("constraint failed")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps SQLiteFullException to TaskDatabaseException`() {
        val ex = SQLiteFullException("db full")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps SQLiteDiskIOException to TaskDatabaseException`() {
        val ex = SQLiteDiskIOException("disk io error")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps SQLiteDatabaseCorruptException to TaskDatabaseException`() {
        val ex = SQLiteDatabaseCorruptException("corrupt")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps SQLiteAccessPermException to TaskDatabaseException`() {
        val ex = SQLiteAccessPermException("perm denied")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps IOException to TaskDatabaseException`() {
        val ex = IOException("io error")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps TimeoutException to TaskDatabaseException`() {
        val ex = TimeoutException("timeout")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskDatabaseException)
        assertEquals("Database error", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps IllegalArgumentException to TaskValidationException`() {
        val ex = IllegalArgumentException("bad arg")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskValidationException)
        assertEquals("bad arg", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `passes through TaskNotFoundException`() {
        val ex = TaskNotFoundException("not found")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskNotFoundException)
        assertEquals("not found", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test
    fun `maps unknown exception to TaskUnknownException`() {
        val ex = RuntimeException("unknown")
        val result = TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
        assertTrue(result is TaskUnknownException)
        assertEquals("unknown", result.message)
        verify(logger).logError(eq(ex), any())
    }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException`() {
        val ex = CancellationException("cancelled")
        TaskRepositoryErrorHandler.handleAndConvert(ex, "testMethod", logger)
    }
} 