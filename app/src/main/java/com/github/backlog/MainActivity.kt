package com.github.backlog

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.backlog.model.DeadlineWorker
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import com.github.backlog.ui.navigation.NavigationRoot
import com.github.backlog.ui.navigation.rememberNavigationState
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.utils.ViewModelFactoryStore
import com.github.backlog.utils.now
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val CHANNEL_ID = "deadline"
private const val WORK_TAG = "deadline"

class MainActivity : ComponentActivity() {
    private lateinit var vmFactories: ViewModelFactoryStore

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        val request = PeriodicWorkRequestBuilder<DeadlineWorker>(1, TimeUnit.HOURS)
            .addTag(WORK_TAG)
            .build()
        val backlogDatabase = (application as BacklogApplication).backlogDatabase

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, request)

        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observeForever {
                scope.launch {
                    backlogDatabase.taskdao()
                        .nonNotifiedTasksWithDeadline()
                        .collect {
                            notificationWorkerObserver(it, notificationBuilder) { taskId ->
                                scope.launch {
                                    backlogDatabase.taskdao()
                                        .markAsNotified(taskId)
                                }
                            }
                        }
                }
            }

        vmFactories = (application as BacklogApplication).viewModelFactoryStore

        setContent {
            BacklogTheme {
                NavigationRoot(rememberNavigationState(vmFactories = vmFactories))
            }
        }
    }

    private inline fun notificationWorkerObserver(
        tasks: List<TaskWithGameTitle>,
        notificationBuilder: NotificationCompat.Builder,
        markAsNotified: (Int) -> Unit
    ) {
        tasks.filter { !it.task.notified }
            .filter { it.task.deadline != null && now() - it.task.deadline > 0L}
            .forEach { task ->
                val builder = notificationBuilder.setSmallIcon(R.drawable.ic_placeholder_image)
                    .setContentTitle(task.gameTitle)
                    .setContentText(task.task.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(this)) {
                    notify(task.task.uid, builder.build())
                }

                markAsNotified(task.task.uid)
            }
    }

    private fun createNotificationChannel() {
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
}
