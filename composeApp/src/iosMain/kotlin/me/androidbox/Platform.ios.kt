package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.core.models.Orientation
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun getOrientation(): Orientation {
    TODO("Not yet implemented")
}

@Composable
actual fun isAtLeastMedium(): Boolean {
    TODO("Not yet implemented")
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
