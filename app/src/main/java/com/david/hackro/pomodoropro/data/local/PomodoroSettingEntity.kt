package com.david.hackro.pomodoropro.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.david.hackro.pomodoropro.domain.CurrentPomodoro

@Entity
class PomodoroSettingEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo
    var period: Long? = null

}