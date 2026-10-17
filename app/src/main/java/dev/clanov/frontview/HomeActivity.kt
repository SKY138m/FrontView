package dev.clanov.frontview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import dev.clanov.frontview.home.HomeScreen
import dev.clanov.frontview.home.LauncherSettings
import dev.clanov.frontview.home.LauncherViewModel

class HomeActivity : ComponentActivity() {
    private val model: LauncherViewModel by viewModels {
        LauncherViewModel.Factory(LauncherSettings(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val slots by model.slots.collectAsStateWithLifecycle()
            MaterialTheme {
                HomeScreen(slots, model::selectSecondary)
            }
        }
    }
}
