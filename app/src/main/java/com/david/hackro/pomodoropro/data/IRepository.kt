package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.domain.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.PomodoroSetting

interface IRepository {

    suspend fun createPomodoro(): CurrentPomodoro?

    suspend fun getPomodoroSetting(): PomodoroSetting
}