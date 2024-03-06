package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.domain.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.PomodoroSetting
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun createPomodoro(): CurrentPomodoro?
    suspend fun stopPomodoro()

    suspend fun getPomodoroSetting(): PomodoroSetting

    fun getPomodorosToday(): Flow<List<Boolean>>
}