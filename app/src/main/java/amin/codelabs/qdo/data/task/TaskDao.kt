package amin.codelabs.qdo.data.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    fun getTaskById(taskId: Long): Flow<TaskEntity?>
} 