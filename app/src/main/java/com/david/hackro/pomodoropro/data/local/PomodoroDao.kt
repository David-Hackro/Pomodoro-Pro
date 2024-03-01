package com.david.hackro.pomodoropro.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentPomodoro(currentPomodoroEntity: CurrentPomodoroEntity): Long

    @Query("SELECT * FROM CurrentPomodoroEntity ORDER BY id DESC LIMIT 1")
    suspend fun getCurrentPomodoro(): CurrentPomodoroEntity

    @Update
    suspend fun updateCurrentPomodoro(currentPomodoroEntity: CurrentPomodoroEntity)

}