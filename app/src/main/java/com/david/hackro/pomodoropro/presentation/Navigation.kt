package com.david.hackro.pomodoropro.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = MAIN_NAV
    ) {
        composable(MAIN_NAV) {
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(SETTING_NAV) {
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}