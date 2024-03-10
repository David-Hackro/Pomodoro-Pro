package com.david.hackro.pomodoropro.domain

import com.david.hackro.pomodoropro.data.IRepository
import javax.inject.Inject

class UpdatePeriodPomodoroSettingsUseCase @Inject constructor(private val repository: IRepository) {

    suspend fun invoke(period: Int) {
        return repository.updatePeriodPomodoroSetting(period)
    }
}