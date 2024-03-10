package com.david.hackro.pomodoropro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.pomodoropro.DEFAULT_PERIOD_TIME
import com.david.hackro.pomodoropro.Millisecond
import com.david.hackro.pomodoropro.SECONDS
import com.david.hackro.pomodoropro.domain.CreatePomodoroUseCase
import com.david.hackro.pomodoropro.domain.DeleteCurrentPomodoroUseCase
import com.david.hackro.pomodoropro.domain.GetPomodoroSettingsUseCase
import com.david.hackro.pomodoropro.domain.GetPomodorosTodayUseCase
import com.david.hackro.pomodoropro.domain.UpdatePeriodPomodoroSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val createPomodoroUseCase: CreatePomodoroUseCase,
    private val getPomodoroSettingsUseCase: GetPomodoroSettingsUseCase,
    private val deleteCurrentPomodoroUseCase: DeleteCurrentPomodoroUseCase,
    private val getPomodoroTodayUseCase: GetPomodorosTodayUseCase,
    private val updatePeriodPomodoroSettingsUseCase: UpdatePeriodPomodoroSettingsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(Pomodoro())
    val state: StateFlow<Pomodoro> = _state.asStateFlow()

    private val _stateSetting = MutableStateFlow(PomodoroSetting())
    val stateSetting: StateFlow<PomodoroSetting> = _stateSetting.asStateFlow()

    private lateinit var updateProgressJob: Job
    val itemList = getPomodoroTodayUseCase.invoke()

    init {
        loadPomodoroSetting()
    }

    private fun loadPomodoroSetting() = viewModelScope.launch {
        val result = getPomodoroSettingsUseCase.invoke()
        _stateSetting.update {
            it.copy(
                period = (result.period / SECONDS / Millisecond).toInt()
            )
        }
    }

    private fun updateProgress() {
        updateProgressJob = viewModelScope.launch {
            val period = getPomodoroSettingsUseCase.invoke().period

            while ((System.currentTimeMillis() - _state.value.startTime) < period) {
                val secondsCompleted = getSecondsCompleted()

                _state.update {
                    it.copy(
                        secondsCompleted = secondsCompleted.toFloat(),
                        isWithoutAnimation = false
                    )
                }

                delay(state.value.animationTime)
            }

            stopPomodoro(true)
        }
    }

    private fun getSecondsCompleted() = (System.currentTimeMillis() - state.value.startTime)

    fun startPomodoro() = viewModelScope.launch {
        val result = createPomodoroUseCase.invoke()
        val period = getPomodoroSettingsUseCase.invoke().period

        if (result == null) {
            return@launch
        }

        _state.update {
            Pomodoro(
                startTime = result.startTime,
                isPomodoroRunning = true,
                secondsCompleted = period.toFloat() / 6,
                period = period,
            )
        }

        delay(state.value.animationTime)

        updateProgress()
    }

    fun stopPomodoro(isPomodoroCompleted: Boolean = false) = viewModelScope.launch {
        deleteCurrentPomodoroUseCase.invoke()

        _state.update {
            Pomodoro(
                isWithoutAnimation = true,
                period = state.value.period,
                isPomodoroCompleted = isPomodoroCompleted
            )
        }

        updateProgressJob.cancel()
    }

    fun updatePeriod(period: Int) = viewModelScope.launch {
        updatePeriodPomodoroSettingsUseCase.invoke(period)
        loadPomodoroSetting()
    }

    data class Pomodoro(
        val startTime: Long = 0L,
        val isWithoutAnimation: Boolean = false,
        val animationTime: Long = 1L,
        var secondsCompleted: Float = 0f,
        var isPomodoroRunning: Boolean = false,
        val period: Long = DEFAULT_PERIOD_TIME,
        val pomodoroByDay: List<Boolean> = listOf(),
        val isPomodoroCompleted: Boolean = false
    )

    data class PomodoroSetting(val period: Int = 0)
}
