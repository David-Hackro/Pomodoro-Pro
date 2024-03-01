package com.david.hackro.pomodoropro.domain

import com.david.hackro.pomodoropro.data.IRepository
import javax.inject.Inject

class DeleteCurrentPomodoroUseCase @Inject constructor(private val repository: IRepository) {

    suspend fun invoke() {
        return repository.stopPomodoro()
    }
}