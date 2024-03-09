package com.david.hackro.pomodoropro.data

import com.david.hackro.pomodoropro.data.local.CurrentPomodoroEntity
import com.david.hackro.pomodoropro.data.local.PomodoroDao
import com.david.hackro.pomodoropro.data.local.PomodoroSettingEntity
import com.david.hackro.pomodoropro.data.local.toDomain
import com.david.hackro.pomodoropro.domain.CurrentPomodoro
import com.david.hackro.pomodoropro.domain.PomodoroSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

const val INVALID_REGISTER = -1L
const val DEFAULT_PERIOD_TIME = 25 * 60 * 1000L // 25 Min
class RepositoryImpl @Inject constructor(private val localSource: PomodoroDao) : IRepository {

    private suspend fun loadDefaultSetting() {
        val setting = PomodoroSettingEntity().apply { period = 1 * 20 * 1000L }
        localSource.insertCurrentSettingPomodoro(setting)
    }

    override suspend fun createPomodoro(): CurrentPomodoro? {
        if (localSource.getCurrentSettingPomodoro() == null) {
            loadDefaultSetting()
        }

        val currentTime = System.currentTimeMillis()
        val currentSetting = localSource.getCurrentSettingPomodoro()
        val entity = CurrentPomodoroEntity().apply {
            startTime = currentTime
            period = currentSetting?.period
        }

        val resultInsert = localSource.insertCurrentPomodoro(entity).run {
            this != INVALID_REGISTER
        }
        val result = localSource.getCurrentPomodoro().toDomain()

        return if (resultInsert) result else null
    }

    override suspend fun stopPomodoro() {
        localSource.run {
            val currentSetting = getCurrentSettingPomodoro()
            val currentPomodoro = getCurrentPomodoro()
            val isCompletedPomodoro = isCompletedPomodoro(currentPomodoro, currentSetting)

            currentPomodoro.apply {
                endTime = System.currentTimeMillis()
                period = currentSetting?.period
                isCompleted = isCompletedPomodoro
            }

            updateCurrentPomodoro(currentPomodoro)
        }
    }

    private fun isCompletedPomodoro(
        currentPomodoro: CurrentPomodoroEntity,
        currentSetting: PomodoroSettingEntity?
    ): Boolean {
        return System.currentTimeMillis() - (currentPomodoro.startTime
            ?: 0L) >= (currentSetting?.period ?: 0)
    }

    override suspend fun getPomodoroSetting(): PomodoroSetting {
        val minutes: Long = localSource.getCurrentSettingPomodoro()?.period ?: 0

        return PomodoroSetting(minutes)
    }

    override fun getPomodorosToday(): Flow<List<Boolean>> {
        val startOfDay = getStartOfDayTimestamp()
        val endOfDay = getEndOfDayTimestamp()

        return localSource.getPomodorosToday(startOfDay, endOfDay)
            .map { pomodoroList ->

                pomodoroList.run {
                    //Return all pomodoros except the current pomodoro that is running
                    if (isNotEmpty() && last().endTime == null) {
                        subList(0, size - 1).map { it.isCompleted ?: false }
                    } else {
                        map { it.isCompleted ?: false }
                    }
                }
            }
    }

    private fun getStartOfDayTimestamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    }

    private fun getEndOfDayTimestamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }
}
