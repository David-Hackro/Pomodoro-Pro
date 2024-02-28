package com.david.hackro.pomodoropro.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.david.hackro.pomodoropro.domain.CurrentPomodoro

@Entity
class CurrentPomodoroEntity {

    @PrimaryKey
    var id: Int? = null

    @ColumnInfo
    var startTime: Long? = null
}


fun CurrentPomodoroEntity.toDomain() = CurrentPomodoro(id ?: 0, startTime ?: 0L)