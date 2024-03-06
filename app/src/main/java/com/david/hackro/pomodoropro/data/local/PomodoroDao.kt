package com.david.hackro.pomodoropro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentPomodoro(currentPomodoroEntity: CurrentPomodoroEntity): Long

    @Query("SELECT * FROM CurrentPomodoroEntity ORDER BY id DESC LIMIT 1")
    suspend fun getCurrentPomodoro(): CurrentPomodoroEntity

    @Update
    suspend fun updateCurrentPomodoro(currentPomodoroEntity: CurrentPomodoroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentSettingPomodoro(pomodoroSettingEntity: PomodoroSettingEntity): Long

    @Query("SELECT * FROM PomodoroSettingEntity ORDER BY id DESC LIMIT 1")
    suspend fun getCurrentSettingPomodoro(): PomodoroSettingEntity

    @Update
    suspend fun updateCurrentSettingPomodoro(pomodoroSettingEntity: PomodoroSettingEntity)
    @Query("SELECT * FROM CurrentPomodoroEntity")
    fun getPomodorosToday(): Flow<List<CurrentPomodoroEntity>>
}