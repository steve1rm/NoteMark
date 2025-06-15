package me.androidbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()

        UUID.randomUUID().toString()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value.showSplash
            }
        }


        enableEdgeToEdge()

        setContent {
            App()
        }

    }

    @Preview
    @Composable
    fun AppAndroidPreview() {
        App()
    }
}