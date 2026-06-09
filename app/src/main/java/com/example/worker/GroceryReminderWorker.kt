package com.example.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.data.DatabaseProvider
import com.example.R

class GroceryReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val db = DatabaseProvider.getDatabase(applicationContext)
        val dao = db.groceryItemDao()

        val activeItems = dao.getActiveItemsOnce().map { it.name.lowercase() }
        val historyItems = dao.getHistoryItemsOnce()

        // Group history by name
        val groupedHistory = historyItems.groupBy { it.name.lowercase() }
        val itemsToRemind = mutableListOf<String>()

        val currentTime = System.currentTimeMillis()
        val THREE_DAYS_MS = 3L * 24 * 60 * 60 * 1000

        for ((name, items) in groupedHistory) {
            // Need to be a frequent item (purchased at least 2 times)
            if (items.size >= 2) {
                // If it's not currently in the active layout
                if (!activeItems.contains(name)) {
                    val latestPurchase = items.maxByOrNull { it.timestamp }
                    if (latestPurchase != null) {
                        val timeSinceLastPurchase = currentTime - latestPurchase.timestamp
                        // If it's been more than 3 days since the last purchase
                        if (timeSinceLastPurchase > THREE_DAYS_MS) {
                            itemsToRemind.add(items.first().name)
                        }
                    }
                }
            }
        }

        if (itemsToRemind.isNotEmpty()) {
            showNotification(itemsToRemind)
        }

        return Result.success()
    }

    private fun showNotification(items: List<String>) {
        val context = applicationContext
        val channelId = "grocery_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Grocery Reminders"
            val descriptionText = "Reminders for frequently purchased items"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val itemText = if (items.size > 3) {
            "${items.take(3).joinToString(", ")} and ${items.size - 3} more"
        } else {
            items.joinToString(", ")
        }

        val builder = NotificationCompat.Builder(context, channelId)
            // Use standard Android icon since we may not have app specific ones handy
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Running low on groceries?")
            .setContentText("You might need to restock: $itemText")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify(1, builder.build())
        }
    }
}
