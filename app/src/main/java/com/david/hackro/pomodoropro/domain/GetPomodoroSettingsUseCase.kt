package com.david.hackro.pomodoropro.domain

import com.david.hackro.pomodoropro.data.IRepository
import com.david.hackro.pomodoropro.domain.model.PomodoroSettingModel
import javax.inject.Inject

class GetPomodoroSettingsUseCase @Inject constructor(private val repository: IRepository) {

    suspend fun invoke(): PomodoroSettingModel {
        return repository.getPomodoroSetting()
    }
}