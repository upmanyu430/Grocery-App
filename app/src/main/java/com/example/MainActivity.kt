package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.GroceryRepository
import com.example.ui.GroceryApp
import com.example.ui.GroceryViewModel
import com.example.ui.theme.MyApplicationTheme

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.worker.GroceryReminderWorker
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        }
    }

    val workRequest = PeriodicWorkRequestBuilder<GroceryReminderWorker>(1, TimeUnit.DAYS).build()
    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
        "GroceryReminder",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )

    val db = com.example.data.DatabaseProvider.getDatabase(applicationContext)
    
    val repository = GroceryRepository(db.groceryItemDao())
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GroceryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val viewModel: GroceryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
            GroceryApp(viewModel)
        }
      }
    }
  }
}
