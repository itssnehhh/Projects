package com.example.standupapplication.ui.screen

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.standupapplication.R
import com.example.standupapplication.receiver.cancelAlarm
import com.example.standupapplication.receiver.scheduleAlarm
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val context = LocalContext.current
    var alarmEnabled by remember { mutableStateOf(false) }
    var reminderTime by remember { mutableStateOf(Calendar.getInstance()) }
    var remainingTime by remember { mutableStateOf(calculateRemainingTime(reminderTime)) }
    var elapsedStartTime by remember { mutableStateOf<Long?>(null) }
    var elapsedEndTime by remember { mutableStateOf<Long?>(null) }
    var elapsedTime by remember { mutableStateOf("00:00:00") }

    val calendar = Calendar.getInstance()
    val hourOfDay = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val time = remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            reminderTime.set(Calendar.HOUR_OF_DAY, mHour)
            reminderTime.set(Calendar.MINUTE, mMinute)
            time.value = "$mHour:$mMinute"
        }, hourOfDay, minute, false
    )

    LaunchedEffect(alarmEnabled) {
        while (alarmEnabled) {
            remainingTime = calculateRemainingTime(reminderTime)
            if (elapsedStartTime != null) {
                elapsedTime = calculateElapsedTime(elapsedStartTime!!)
            }
            delay(1000L)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2E159C)),
                title = {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFDADFF8))
        ) {
            item {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.anim1)
                )
                LottieAnimation(
                    composition = composition, modifier = Modifier
                        .padding(16.dp)
                        .border(2.dp, Color.Gray)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF2E159C)),
                        onClick = {
                            alarmEnabled = false
                            elapsedStartTime = null
                            elapsedEndTime = null
                            elapsedTime = "00:00:00"
                            remainingTime = "00:00:00"
                            cancelAlarm(context)
                        },
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Reset")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF2E159C)),
                        onClick = {
                            if (alarmEnabled) {
                                elapsedEndTime = System.currentTimeMillis()
                            } else {
                                elapsedStartTime = System.currentTimeMillis()
                                scheduleAlarm(context, reminderTime)
                            }
                            alarmEnabled = !alarmEnabled
                        },
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp)
                    ) {
                        Text(text = if (alarmEnabled) "Stop" else "Start")
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(1.dp, Color.LightGray, CardDefaults.shape),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color(0xFF3F1EBB))
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " Alarm Mode",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )
                        Switch(
                            checked = alarmEnabled,
                            onCheckedChange = {
                                alarmEnabled = !alarmEnabled
                                if (alarmEnabled) {
                                    elapsedStartTime = System.currentTimeMillis()
                                    scheduleAlarm(context, reminderTime)
                                } else {
                                    elapsedEndTime = System.currentTimeMillis()
                                    cancelAlarm(context)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                uncheckedThumbColor = Color.DarkGray,
                                checkedTrackColor = Color.DarkGray,
                                uncheckedTrackColor = Color.White,
                                checkedBorderColor = Color.LightGray,
                                uncheckedBorderColor = Color.DarkGray
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .border(1.dp, Color.LightGray, CardDefaults.shape),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(Color(0xFF3F1EBB))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " Select the timer",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.timer),
                            contentDescription = "",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    timePickerDialog.show()
                                }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Reminder Time :- ",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = time.value,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Total Elapsed Time :- ",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = elapsedTime,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun calculateRemainingTime(reminderTime: Calendar): String {
    val currentTime = Calendar.getInstance()
    val differenceInMillis = reminderTime.timeInMillis - currentTime.timeInMillis

    return if (differenceInMillis > 0) {
        val hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMillis) % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        "00:00:00"
    }
}

@SuppressLint("DefaultLocale")
fun calculateElapsedTime(startTime: Long): String {
    val currentTime = System.currentTimeMillis()
    val differenceInMillis = currentTime - startTime

    val hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMillis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}