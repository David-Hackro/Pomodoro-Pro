package com.david.hackro.pomodoropro.domain

import com.david.hackro.pomodoropro.data.IRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPomodorosTodayUseCase @Inject constructor(private val repository: IRepository) {

    fun invoke(): Flow<List<Boolean>> {
        return repository.getPomodorosToday()
    }
}