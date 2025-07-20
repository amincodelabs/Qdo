package amin.codelabs.qdo

import amin.codelabs.qdo.ui.QdoNavHost
import amin.codelabs.qdo.ui.theme.QdoTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            QdoTheme {
                val navController = rememberNavController()
                QdoNavHost(navController = navController)
            }
        }
    }
}