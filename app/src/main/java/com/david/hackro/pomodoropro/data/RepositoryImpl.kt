package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.data.local.CurrentPomodoroEntity
import com.david.hackro.pomodoropro.data.local.PomodoroDao
import com.david.hackro.pomodoropro.domain.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.PomodoroSetting
import javax.inject.Inject

const val INVALID_REGISTER = -1L

class RepositoryImpl @Inject constructor(private val localSource: PomodoroDao) : IRepository {

    override suspend fun createPomodoro(): CurrentPomodoro? {
        val currentTime = System.currentTimeMillis()
        val entity = CurrentPomodoroEntity().apply { startTime = currentTime }
        val result = localSource.insertCurrentPomodoro(entity).run {
            this != INVALID_REGISTER
        }

        return if (result) {
            CurrentPomodoro(0, currentTime)
        } else {
            null
        }
    }

    override suspend fun stopPomodoro() {
        localSource.run {
            val currentPomodoro = getCurrentPomodoro()
            currentPomodoro.apply {
                endTime = System.currentTimeMillis()
            }

            updateCurrentPomodoro(currentPomodoro)
        }
    }

    override suspend fun getPomodoroSetting(): PomodoroSetting {
        val minutes = 25 * 60 * 1000L
        return PomodoroSetting(0, minutes)
    }
}
