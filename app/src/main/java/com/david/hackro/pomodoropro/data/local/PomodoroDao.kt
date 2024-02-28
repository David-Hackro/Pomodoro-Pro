package com.david.hackro.pomodoropro.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PomodoroDao {

    @Insert
    suspend fun insertCurrentPomodoro(currentPomodoroEntity: CurrentPomodoroEntity): Long

}