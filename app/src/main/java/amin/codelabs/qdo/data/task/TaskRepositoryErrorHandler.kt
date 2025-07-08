package amin.codelabs.qdo.data.task

import amin.codelabs.qdo.domain.task.TaskRepositoryException
import amin.codelabs.qdo.domain.task.TaskNotFoundException
import amin.codelabs.qdo.domain.task.TaskValidationException
import amin.codelabs.qdo.domain.task.TaskDatabaseException
import amin.codelabs.qdo.domain.task.TaskUnknownException
import amin.codelabs.qdo.infrastructure.logger.Logger
import java.util.concurrent.CancellationException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteFullException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteAccessPermException
import java.io.IOException
import java.util.concurrent.TimeoutException

object TaskRepositoryErrorHandler {
    fun handleAndConvert(e: Throwable, method: String, logger: Logger): TaskRepositoryException {
        if (e is CancellationException) throw e // Don't catch coroutine cancellations
        logger.logError(e, "TaskRepositoryImpl.$method error: ${e.javaClass.simpleName}: ${e.message}")
        return when (e) {
            is SQLiteConstraintException,
            is SQLiteFullException,
            is SQLiteDiskIOException,
            is SQLiteDatabaseCorruptException,
            is SQLiteAccessPermException,
            is IOException,
            is TimeoutException -> TaskDatabaseException("Database error", e)
            is IllegalArgumentException -> TaskValidationException(e.message ?: "Invalid argument", e)
            is TaskNotFoundException -> e
            else -> TaskUnknownException(e.message ?: "Unknown error", e)
        }
    }
} 