package com.example.p_shedulealarm

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.p_shedulealarm.ui.theme.P_SheduleAlarmTheme
import java.time.LocalDateTime
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scheduler = AndroidAlarmScheduler(this)

        enableEdgeToEdge()
        setContent {
            P_SheduleAlarmTheme {
                var secondsText by remember { mutableStateOf("") }
                var message by remember { mutableStateOf("") }
                // Move alarmItem state into Compose
                var alarmItem by remember { mutableStateOf<AlarmItem?>(null) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = secondsText,
                        onValueChange = { secondsText = it},
                        label = { Text(text = "enter seconds") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Trigger Alarm in seconds")
                        }
                    )
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it},
                        label = { Text(text = "enter message") },  // Fixed label text
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Message")
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)  // Add spacing between buttons
                    ) {
                        Button(
                            onClick = {
                                try {
                                    val newAlarmItem = AlarmItem(
                                        time = LocalDateTime.now().plusSeconds(secondsText.toLong()),
                                        message = message
                                    )
                                    alarmItem = (newAlarmItem)  // Update the alarm item
                                    scheduler.schedule(newAlarmItem)
                                    secondsText = "" // Clear the fields
                                    message = ""
                                } catch (e: NumberFormatException) {
                                    // Handle invalid number input
                                    // You might want to show an error message to the user
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Schedule")
                        }
                        Button(
                            onClick = {
                                alarmItem?.let {
                                    scheduler.cancel(it)
                                    alarmItem = (null)  // Clear the alarm item
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }

            }
        }
    }


