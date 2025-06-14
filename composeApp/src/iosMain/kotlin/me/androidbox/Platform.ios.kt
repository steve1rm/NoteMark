package me.androidbox

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import me.androidbox.core.models.Orientation
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun getOrientation(): Orientation {
    // UIDevice.currentDevice.orientation is not reliable in Compose context
    // Using screen dimensions is more reliable
    val screenWidth = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeight = LocalDensity.current.run { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    return if (screenWidth < screenHeight) {
        Orientation.PORTRAIT
    } else {
        Orientation.LANDSCAPE
    }
}

@Composable
actual fun isAtLeastMedium(): Boolean {
    // Medium width threshold (similar to Android's WIDTH_DP_MEDIUM_LOWER_BOUND)
    val mediumWidthThreshold = 600
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    return screenWidthDp >= mediumWidthThreshold
}

actual class NoteMarkPreferencesImp : NoteMarkPreferences {
    override fun setRefreshToken(value: String) {
        TODO("Not yet implemented")
    }

    override fun getRefreshToken(): String {
        TODO("Not yet implemented")
    }

    override fun setAccessToken(value: String) {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(): String {
        TODO("Not yet implemented")
    }
}
