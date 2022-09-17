package com.github.backlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.backlog.ui.navigation.NavigationRoot
import com.github.backlog.ui.navigation.rememberNavigationState
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.utils.ViewModelFactoryStore

class MainActivity : ComponentActivity() {
    private lateinit var vmFactories: ViewModelFactoryStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        vmFactories = (application as BacklogApplication).viewModelFactoryStore

        setContent {
            BacklogTheme {
                NavigationRoot(rememberNavigationState(vmFactories = vmFactories))
            }
        }
    }
}

private fun MainActivity.createNotificationChannel() {
    val name = "task channel"
    val descriptionText = ""
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
    }
    // Register the channel with the system
    val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
