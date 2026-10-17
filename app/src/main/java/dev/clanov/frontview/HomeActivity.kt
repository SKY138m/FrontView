package dev.clanov.frontview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.clanov.frontview.home.HomeScreen
import dev.clanov.frontview.home.LauncherSettings
import dev.clanov.frontview.home.LauncherViewModel

class HomeActivity : ComponentActivity() {
    private val model: LauncherViewModel by viewModels {
        LauncherViewModel.Factory(LauncherSettings(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val layout by model.layout.collectAsStateWithLifecycle()
            MaterialTheme {
                HomeScreen(layout, model::place, ::openApp)
            }
        }
    }

    private fun openApp(packageName: String) {
        packageManager.getLaunchIntentForPackage(packageName)?.let(::startActivity)
    }
}
