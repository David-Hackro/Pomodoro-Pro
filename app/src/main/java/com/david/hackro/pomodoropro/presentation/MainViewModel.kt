package com.david.hackro.pomodoropro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.pomodoropro.MAX_TIME_IN_MIN
import com.david.hackro.pomodoropro.animationTime
import com.david.hackro.pomodoropro.domain.CreatePomodoroUseCase
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
    private val getPomodoroSettingsUseCase: GetPomodoroSettingsUseCase
) :
    ViewModel() {

    private val _state = MutableStateFlow(Pomodoro())
    val state: StateFlow<Pomodoro> = _state.asStateFlow()
    private var startTime: Long = 0L
    private lateinit var updateProgressJob: Job


    private fun updateProgress() {
        updateProgressJob = viewModelScope.launch {

            for (i in 0..MAX_TIME_IN_MIN) {
                val secondsCompleted = getSecondsCompleted() * 1000

                _state.update {
                    it.copy(time = secondsCompleted.toFloat())
                }

                delay(animationTime.toLong())
            }
        }
    }

    private fun getSecondsCompleted() = ((getCurrentTimeStand() - state.value.startTime) / 1000)

    fun startPomodoro() = viewModelScope.launch {
        val result = createPomodoroUseCase.invoke()

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
                isFirstTime = true,
                isPomodoroRunning = true,
                time = 0.1f
            )
        }

        updateProgress()
    }

    fun stopPomodoro() {
        _state.update {
            Pomodoro()
        }

        updateProgressJob.cancel()
    }

    private fun getCurrentTimeStand(): Long {
        return System.currentTimeMillis()
    }

    data class Pomodoro(
        val startTime: Long = 0L,
        val isFirstTime: Boolean = false,
        var time: Float = 0f,
        var isPomodoroRunning: Boolean = false
    )
}
