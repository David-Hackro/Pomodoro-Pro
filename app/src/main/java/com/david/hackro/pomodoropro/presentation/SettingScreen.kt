package com.david.hackro.pomodoropro.presentation

import android.content.Context
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.david.hackro.pomodoropro.R

const val SETTING_NAV = "setting"

@Composable
fun SettingScreen(navController: NavController, viewModel: MainViewModel) {
    Box(modifier = Modifier.padding(24.dp)) {
        Column {

            Text(
                text = stringResource(id = R.string.setting_period),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )

            DropdownMenuExample(viewModel)

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            )
        }
    }
}

@Composable
fun DropdownMenuExample(viewModel: MainViewModel) {
    var menuExpanded by remember { mutableStateOf(false) }
    val uiState = viewModel.stateSetting.collectAsState()
    var selectedOption by remember { mutableIntStateOf(uiState.value.period) }
    val context = LocalContext.current

    Box {

        Button(
            onClick = { menuExpanded = !menuExpanded },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.width(200.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(stringResource(id = R.string.current_period, selectedOption))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            val values = listOf(15, 25, 45, 60)

            for (value in values) {
                DropdownMenuItem(
                    onClick = {
                        selectedOption = value
                        menuExpanded = false
                        viewModel.updatePeriod(value)
                        showConfirmationMessage(context)
                    },
                    modifier = Modifier.width(150.dp), text = {
                        Text(stringResource(id = R.string.minutes, value))
                    })

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )

            }

        }
    }
}

fun showConfirmationMessage(context: Context) {
    Toast.makeText(context, "Periodo Actualizado", Toast.LENGTH_SHORT).show()
}
