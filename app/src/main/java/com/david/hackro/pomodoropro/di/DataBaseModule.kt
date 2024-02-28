package com.david.hackro.pomodoropro.di

import android.content.Context
import androidx.room.Room
import com.david.hackro.pomodoropro.data.local.AppDataBase
import com.david.hackro.pomodoropro.data.local.PomodoroDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "pomodoro"

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context = context, AppDataBase::class.java, DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun providePomodoroDao(appDataBase: AppDataBase): PomodoroDao {
        return appDataBase.pomodoroDao()
    }

}