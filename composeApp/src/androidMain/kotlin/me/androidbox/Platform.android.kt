package me.androidbox

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import androidx.window.layout.adapter.computeWindowSizeClass
import me.androidbox.core.models.Orientation

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getOrientation(): Orientation {
    val configuration = LocalConfiguration.current

    return when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Orientation.PORTRAIT
        }

        Configuration.ORIENTATION_LANDSCAPE -> {
            Orientation.LANDSCAPE
        }

        else -> {
            Orientation.PORTRAIT
        }
    }
}

@Composable
actual fun isAtLeastMedium(): Boolean {
    val sizeClass = calculateWindowSizeClass()
    return sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
}

@Composable
fun calculateWindowSizeClass(): WindowSizeClass {
    val currentWindowMetrics =
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(LocalContext.current)
    return WindowSizeClass.BREAKPOINTS_V1.computeWindowSizeClass(currentWindowMetrics)
}

actual class NoteMarkPreferencesImp(context: Context) : NoteMarkPreferences {
    actual override fun setRefreshToken(value: String) {
//        TODO("Not yet implemented")
    }

    actual override fun getRefreshToken(): String {
//        TODO("Not yet implemented")
        return ""
    }

    actual override fun setAccessToken(value: String) {
//        TODO("Not yet implemented")
    }

    actual override fun getAccessToken(): String {
//        TODO("Not yet implemented")
        return ""
    }
}