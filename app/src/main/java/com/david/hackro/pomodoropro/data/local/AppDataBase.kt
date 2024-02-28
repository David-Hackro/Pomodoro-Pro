package com.david.hackro.pomodoropro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_VERSION = 1

@Database(
    version = DATABASE_VERSION,
    entities = [CurrentPomodoroEntity::class]
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao
}

