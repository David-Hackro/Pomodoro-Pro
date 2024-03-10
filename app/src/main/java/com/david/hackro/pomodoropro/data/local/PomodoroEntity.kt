package com.david.hackro.pomodoropro.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.david.hackro.pomodoropro.domain.model.CurrentPomodoro

@Entity
class CurrentPomodoroEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo
    var startTime: Long? = null

    @ColumnInfo
    var endTime: Long? = null

    @ColumnInfo
    var period: Long? = null

    @ColumnInfo
    var isCompleted: Boolean? = null
}


fun CurrentPomodoroEntity.toDomain() = CurrentPomodoro(id ?: 0, startTime ?: 0L)