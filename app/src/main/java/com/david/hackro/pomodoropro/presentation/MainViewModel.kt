package com.david.hackro.pomodoropro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.pomodoropro.domain.CreatePomodoroUseCase
import com.david.hackro.pomodoropro.domain.DeleteCurrentPomodoroUseCase
import com.david.hackro.pomodoropro.domain.GetPomodoroSettingsUseCase
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
) :
    ViewModel() {

    private val _state = MutableStateFlow(Pomodoro())
    val state: StateFlow<Pomodoro> = _state.asStateFlow()
    private lateinit var updateProgressJob: Job

    init {
        viewModelScope.launch {
            val period = getPomodoroSettingsUseCase.invoke().period

            _state.update {
                Pomodoro(isWithoutAnimation = true, period = period)
            }
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

            stopPomodoro()
        }
    }

    private fun getSecondsCompleted() = (System.currentTimeMillis() - state.value.startTime)

    fun startPomodoro() = viewModelScope.launch {
        val result = createPomodoroUseCase.invoke()
        val period = getPomodoroSettingsUseCase.invoke().period

        if (result == null) {
            _state.update {
                it
            }
            //happend something
            return@launch
        }

        _state.update {
            it.copy(
                startTime = result.startTime,
                isWithoutAnimation = false,
                isPomodoroRunning = true,
                secondsCompleted = period.toFloat() / 6,
                period = period,
            )
        }

        delay(state.value.animationTime)

        updateProgress()
    }

    fun stopPomodoro() = viewModelScope.launch {
        deleteCurrentPomodoroUseCase.invoke()

        _state.update {
            Pomodoro(isWithoutAnimation = true, period = state.value.period)
        }

        updateProgressJob.cancel()
    }

    data class Pomodoro(
        val startTime: Long = 0L,
        val isWithoutAnimation: Boolean = false,
        val animationTime: Long = 1L,
        var secondsCompleted: Float = 0f,
        var isPomodoroRunning: Boolean = false,
        val period: Long = 25 * 60 * 1000L
    )

}
