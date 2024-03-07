package com.david.hackro.pomodoropro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.david.hackro.pomodoropro.R
import com.david.hackro.pomodoropro.presentation.ui.components.CircularProgressbar
import com.david.hackro.pomodoropro.presentation.ui.theme.BackgroundColor
import com.david.hackro.pomodoropro.presentation.ui.theme.PomodoroIncompleteColor
import com.david.hackro.pomodoropro.presentation.ui.theme.PomodoroProTheme
import com.david.hackro.pomodoropro.presentation.ui.theme.Purple40
import com.david.hackro.pomodoropro.presentation.ui.theme.StartButtonColor
import com.david.hackro.pomodoropro.presentation.ui.theme.StopButtonColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroProTheme {

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
    val pomodorosTodayStatus: State<List<Boolean>> =
        viewModel.getPomodoroTodayUseCase.invoke().collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(modifier = Modifier.height(30.dp).background(BackgroundColor)) {

            LazyRow(modifier = Modifier
                .wrapContentHeight()
                .width(200.dp)) {
                items(items = pomodorosTodayStatus.value, itemContent = { item ->
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Favorite",
                        tint = if (item) Purple40 else PomodoroIncompleteColor
                    )
                })
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Row { CircularProgressbar(uiState = uiState) }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Row {
            if (uiState.isPomodoroRunning) {
                Button(
                    onClick = { viewModel.stopPomodoro() },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = StopButtonColor,
                        contentColor = colorResource(id = R.color.white)
                    )
                ) { Text(text = stringResource(R.string.stop_pomodoro)) }
            } else {
                Button(
                    onClick = { viewModel.startPomodoro() },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = StartButtonColor,
                        contentColor = colorResource(id = R.color.white)
                    )
                ) { Text(text = stringResource(R.string.start_pomodoro)) }
            }
        }
    }
}
