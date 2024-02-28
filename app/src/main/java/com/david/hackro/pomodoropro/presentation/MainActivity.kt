package com.david.hackro.pomodoropro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.david.hackro.pomodoropro.R
import com.david.hackro.pomodoropro.presentation.ui.components.CircularProgressbar
import com.david.hackro.pomodoropro.presentation.ui.theme.PomodoroProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroProTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    Screen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row { CircularProgressbar(uiState = uiState) }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Row {
            if (uiState.isPomodoroRunning) {
                Button(onClick = { viewModel.stopPomodoro() }) { Text(text = stringResource(R.string.stop_pomodoro)) }
            } else {
                Button(onClick = { viewModel.startPomodoro() }) { Text(text = stringResource(R.string.start_pomodoro)) }
            }
        }
    }
}
