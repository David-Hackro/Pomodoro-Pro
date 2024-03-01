package com.david.hackro.pomodoropro.di

import com.david.hackro.pomodoropro.data.IRepository
import com.david.hackro.pomodoropro.data.RepositoryImpl
import com.david.hackro.pomodoropro.domain.CreatePomodoroUseCase
import com.david.hackro.pomodoropro.domain.GetPomodoroSettingsUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [PomodoroModule.BindRepository::class])
@InstallIn(SingletonComponent::class)
object PomodoroModule {

    @Provides
    @Singleton
    fun provideCreatePomodoroUseCase(repository: IRepository): CreatePomodoroUseCase {
        return CreatePomodoroUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideGetPomodoroSettingsUseCase(repository: IRepository): GetPomodoroSettingsUseCase {
        return GetPomodoroSettingsUseCase(repository)
    }

     @Module
     @InstallIn(SingletonComponent::class)
     interface BindRepository {

         @Binds
         @Singleton
         fun bindRepository(repository: RepositoryImpl): IRepository
     }
}