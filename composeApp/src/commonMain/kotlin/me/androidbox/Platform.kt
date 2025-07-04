package me.androidbox

import androidx.compose.runtime.Composable
import me.androidbox.core.models.Orientation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun generateUUID(): String

@Composable
expect fun getOrientation(): Orientation

@Composable
expect fun OnChangeOrientation(orientation: Orientation)

@Composable
expect fun isAtLeastMedium(): Boolean

expect fun isTablet() : Boolean

expect fun getCurrentMillis() : Long
expect fun Long.formattedDateString() : String

expect fun emailValid(email: String) : Boolean

interface NoteMarkPreferences {
    fun setRefreshToken(value: String)
    fun getRefreshToken(): String?

    fun setAccessToken(value: String)
    fun getAccessToken(): String?
}

expect class NoteMarkPreferencesImp : NoteMarkPreferences {
    override fun setRefreshToken(value: String)
    override fun getRefreshToken(): String?
    override fun setAccessToken(value: String)
    override fun getAccessToken(): String?
}
