package me.androidbox

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import androidx.window.layout.adapter.computeWindowSizeClass
import com.liftric.kvault.KVault
import me.androidbox.core.models.LocalPreferences.ACCESS_TOKEN_KEY
import me.androidbox.core.models.LocalPreferences.REFRESH_TOKEN_KEY
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

actual fun emailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
    private val store = KVault(context, "noteMark")

    actual override fun setRefreshToken(value: String) {
        store.set(REFRESH_TOKEN_KEY, value)
    }

    actual override fun getRefreshToken(): String? {
        return store.string(forKey = REFRESH_TOKEN_KEY)
    }

    actual override fun setAccessToken(value: String) {
        store.set(ACCESS_TOKEN_KEY, value)
    }

    actual override fun getAccessToken(): String? {
        return store.string(forKey = ACCESS_TOKEN_KEY)
    }
}