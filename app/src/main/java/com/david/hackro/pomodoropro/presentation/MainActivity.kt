package com.david.hackro.pomodoropro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.david.hackro.pomodoropro.presentation.ui.theme.PomodoroProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroProTheme {
                val navController: NavHostController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    NavigationHost(navController = navController, viewModel)
                }
            }
        }
    }
}