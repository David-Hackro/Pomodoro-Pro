package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.data.local.CurrentPomodoroEntity
import com.david.hackro.pomodoropro.data.local.PomodoroDao
import com.david.hackro.pomodoropro.data.local.PomodoroSettingEntity
import com.david.hackro.pomodoropro.domain.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.PomodoroSetting
import javax.inject.Inject

const val INVALID_REGISTER = -1L

class RepositoryImpl @Inject constructor(private val localSource: PomodoroDao) : IRepository {

    override suspend fun createPomodoro(): CurrentPomodoro? {
        /*val setting = PomodoroSettingEntity().apply { period = minutes }
        localSource.insertCurrentSettingPomodoro(setting)*/

        val currentTime = System.currentTimeMillis()
        val currentSetting = localSource.getCurrentSettingPomodoro()
        val entity = CurrentPomodoroEntity().apply {
            startTime = currentTime
            period = currentSetting.period
        }

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
            val currentSetting = getCurrentSettingPomodoro()
            val currentPomodoro = getCurrentPomodoro()
            val isCompletedPomodoro =
                ((System.currentTimeMillis() - currentPomodoro.startTime!!) >= currentSetting.period!!)

            currentPomodoro.apply {
                endTime = System.currentTimeMillis()
                period = currentSetting.period
                isCompleted = isCompletedPomodoro
            }

            updateCurrentPomodoro(currentPomodoro)
        }
    }

    override suspend fun getPomodoroSetting(): PomodoroSetting {
        val minutes: Long = localSource.getCurrentSettingPomodoro().period?:0

        return PomodoroSetting(minutes)
    }
}

//const val minutes = 1 * 10 * 1000L