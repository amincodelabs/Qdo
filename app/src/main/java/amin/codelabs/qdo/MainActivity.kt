package amin.codelabs.qdo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import amin.codelabs.qdo.ui.theme.QdoTheme
import amin.codelabs.qdo.ui.QdoNavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QdoTheme {
                val navController = rememberNavController()
                QdoNavHost(navController = navController)
            }
        }
    }
}