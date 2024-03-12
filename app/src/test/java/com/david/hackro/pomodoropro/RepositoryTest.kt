package com.david.hackro.pomodoropro

import com.david.hackro.pomodoropro.data.IRepository
import com.david.hackro.pomodoropro.data.RepositoryImpl
import com.david.hackro.pomodoropro.data.local.CurrentPomodoroEntity
import com.david.hackro.pomodoropro.data.local.PomodoroDao
import com.david.hackro.pomodoropro.data.local.PomodoroSettingEntity
import com.david.hackro.pomodoropro.data.local.toDomain
import com.david.hackro.pomodoropro.domain.model.CurrentPomodoro
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    @RelaxedMockK
    private lateinit var localSource: PomodoroDao

    private lateinit var objectUnderTest: IRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUpRepository()
    }


    @Test
    fun `should createPomodoro and return the pomodoro was created`() {
        runTest {

            val id = 1
            val startTime = 123456L

            val localSettingResponse = PomodoroSettingEntity()
            coEvery { localSource.getCurrentSettingPomodoro() } returns localSettingResponse

            val localCurrentPomodoro = CurrentPomodoroEntity().apply {
                this.id = id
                this.startTime = startTime
            }
            coEvery { localSource.getCurrentPomodoro() } returns localCurrentPomodoro

            val expected = CurrentPomodoro(id, startTime)


            val result = objectUnderTest.createPomodoro()


            coEvery { localSource.getCurrentSettingPomodoro() }


            assertEquals(result, expected)


            coEvery { localSource.insertCurrentPomodoro(any()) }
            coEvery { localSource.getCurrentPomodoro().toDomain() }
        }
    }

    private fun setUpRepository() {
        objectUnderTest = RepositoryImpl(localSource = localSource)
    }
}