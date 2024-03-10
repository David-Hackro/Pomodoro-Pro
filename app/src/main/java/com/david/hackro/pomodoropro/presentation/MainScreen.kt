package com.david.hackro.pomodoropro.presentation

import android.media.MediaPlayer
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.david.hackro.pomodoropro.R
import com.david.hackro.pomodoropro.presentation.ui.components.CircularProgressbar
import com.david.hackro.pomodoropro.presentation.ui.theme.BackgroundColor
import com.david.hackro.pomodoropro.presentation.ui.theme.PomodoroIncompleteColor
import com.david.hackro.pomodoropro.presentation.ui.theme.Purple40
import com.david.hackro.pomodoropro.presentation.ui.theme.StartButtonColor
import com.david.hackro.pomodoropro.presentation.ui.theme.StopButtonColor

const val MAIN_NAV = "main"

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel ) {
    val uiState by viewModel.state.collectAsState()
    val pomodoroTodayStatus: State<List<Boolean>> =
        viewModel.itemList.collectAsState(initial = emptyList())
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current

    DisposableEffect(uiState.isPomodoroCompleted) {
        if (uiState.isPomodoroCompleted) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Settings.System.DEFAULT_NOTIFICATION_URI)
                prepare()
                start()
            }
        }

        onDispose {
            mediaPlayer?.release()
        }
    }

    Box {
        if (uiState.isPomodoroRunning.not()) {
            IconButton(
                onClick = { navController.navigate(SETTING_NAV) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = StartButtonColor
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .height(30.dp)
                    .width(200.dp)
                    .background(BackgroundColor)
            ) {

                LazyRow(
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    items(items = pomodoroTodayStatus.value, itemContent = { item ->
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
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
                val color = if (uiState.isPomodoroRunning) StopButtonColor else StartButtonColor
                val text =
                    if (uiState.isPomodoroRunning) R.string.stop_pomodoro else R.string.start_pomodoro
                val function: () -> Unit =
                    { if (uiState.isPomodoroRunning) viewModel.stopPomodoro() else viewModel.startPomodoro() }

                Button(
                    onClick = function,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = color,
                        contentColor = colorResource(id = R.color.white)
                    )
                ) { Text(text = stringResource(text)) }
            }
        }
    }
}
