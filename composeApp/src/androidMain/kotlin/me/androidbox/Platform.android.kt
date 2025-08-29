package me.androidbox

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.WindowMetricsCalculator
import androidx.window.layout.adapter.computeWindowSizeClass
import com.liftric.kvault.KVault
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import me.androidbox.core.data.models.LocalPreferences.ACCESS_TOKEN_KEY
import me.androidbox.core.data.models.LocalPreferences.REFRESH_TOKEN_KEY
import me.androidbox.core.data.models.LocalPreferences.USER_NAME_KEY
import me.androidbox.core.presentation.models.Orientation
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun generateUUID(): String {
    return UUID.randomUUID().toString()
}

@Composable
actual fun registerBackHandler(onBackPressed: () -> Unit) {
    BackHandler(enabled = true, onBack = onBackPressed)
}

@Composable
actual fun OnChangeOrientation(orientation: Orientation) {
    val context = LocalContext.current
    val activity = context as? Activity
    LaunchedEffect(orientation) {
        val requested = when (orientation) {
            Orientation.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_FULL_USER
            Orientation.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        if (activity?.requestedOrientation != requested) {
            activity?.requestedOrientation = requested
            activity?.overridePendingTransition(0, 0) // optional: suppress flicker
        }
    }

    // Optional: Reset when Composable leaves
    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

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

actual fun getCurrentMillis(): Long {
    return System.currentTimeMillis()
}

@RequiresApi(Build.VERSION_CODES.O)
actual fun Long.formattedDateString(): String {
    val zonedDateTime = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )

    val formatter = if (zonedDateTime.year == ZonedDateTime.now().year) {
        DateTimeFormatter.ofPattern("dd MMM")
    } else DateTimeFormatter.ofPattern("dd MMM yyyy")
    return formatter.format(zonedDateTime)
}

actual fun isTablet(): Boolean {
    val configuration = Resources.getSystem().configuration

    return (configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >=
            Configuration.SCREENLAYOUT_SIZE_LARGE
}

actual class AndroidConnectivityManager(
    private val context: Context
) : ConnectivityManager {
    private val connectivityManager = context.getSystemService<android.net.ConnectivityManager>()
    actual override fun isConnected(): Flow<Boolean> = callbackFlow {
        val networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false).isSuccess
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false).isSuccess
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val isConnected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(isConnected).isSuccess
            }
        }

        connectivityManager?.registerDefaultNetworkCallback(networkCallback)

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }

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

    actual override fun setUserName(value: String) {
        store.set(USER_NAME_KEY, value)
    }

    actual override fun getUserName(): String? {
        return store.string(forKey = USER_NAME_KEY)
    }

    actual override fun deleteAllPreferences() {
        store.clear()
    }
}