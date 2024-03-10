package com.david.hackro.pomodoropro.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PomodoroSettingEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo
    var period: Long = 0
}