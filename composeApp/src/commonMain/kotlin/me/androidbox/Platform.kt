package me.androidbox

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import me.androidbox.core.presentation.models.Orientation

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

@Composable
expect fun registerBackHandler(onBackPressed : () -> Unit)

interface NoteMarkPreferences {
    fun setRefreshToken(value: String)
    fun getRefreshToken(): String?

    fun setAccessToken(value: String)
    fun getAccessToken(): String?

    fun setUserName(value: String)
    fun getUserName(): String?

    fun deleteAllPreferences()
}

interface ConnectivityManager {
    fun isConnected() : Flow<Boolean>
}

expect class AndroidConnectivityManager : ConnectivityManager {
    override fun isConnected(): Flow<Boolean>
}

expect class NoteMarkPreferencesImp : NoteMarkPreferences {
    override fun setRefreshToken(value: String)
    override fun getRefreshToken(): String?
    override fun setAccessToken(value: String)
    override fun getAccessToken(): String?
    override fun deleteAllPreferences()

    override fun setUserName(value: String)
    override fun getUserName(): String?
}
