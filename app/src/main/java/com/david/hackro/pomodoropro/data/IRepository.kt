package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.domain.model.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.model.PomodoroSettingModel
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun createPomodoro(): CurrentPomodoro
    suspend fun stopPomodoro()

    suspend fun getPomodoroSetting(): PomodoroSettingModel

    suspend fun updatePeriodPomodoroSetting(period: Int)

    fun getPomodorosToday(): Flow<List<Boolean>>
}