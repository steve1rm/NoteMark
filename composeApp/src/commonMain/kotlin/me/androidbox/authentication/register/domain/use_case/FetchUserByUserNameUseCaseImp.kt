package me.androidbox.authentication.register.domain.use_case

import me.androidbox.NoteMarkPreferences
import me.androidbox.user.data.UserLocalDataSource
import net.orandja.either.Left
import net.orandja.either.Right

class FetchUserByUserNameUseCaseImp(
    private val noteMarkPreferences: NoteMarkPreferences,
    private val userLocalDataSource: UserLocalDataSource
) {
    suspend fun execute(): String? {
        val userName = noteMarkPreferences.getUserName()

        if(userName == null) {
            return null
        }
        else {
            val localResult = userLocalDataSource.fetchUserByUserName(userName)

            return when(localResult) {
                is Left -> {
                    localResult.left.userId
                }
                is Right -> {
                   null
                }
            }
        }
    }
}