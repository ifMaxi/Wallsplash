package com.maxidev.wallsplash

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.maxidev.wallsplash.feature.navigation.NavigationGraph
import com.maxidev.wallsplash.feature.theme.WallsplashTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced
        }
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            WallsplashTheme {
                Surface {
                    NavigationGraph()
                }
            }
        }
    }
}