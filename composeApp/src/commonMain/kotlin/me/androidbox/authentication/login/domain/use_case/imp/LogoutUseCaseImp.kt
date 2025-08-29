package me.androidbox.authentication.login.domain.use_case.imp

import me.androidbox.NoteMarkPreferences
import me.androidbox.authentication.login.domain.model.LogoutRequest
import me.androidbox.authentication.login.domain.use_case.LogoutUseCase
import me.androidbox.authentication.register.domain.AuthorizationRepository
import me.androidbox.core.data.models.DataError
import me.androidbox.core.domain.SyncNoteScheduler
import me.androidbox.notes.domain.usecases.NukeAllNotesUseCase
import net.orandja.either.Either
import net.orandja.either.Left
import net.orandja.either.Right

class LogoutUseCaseImp(
    private val authorizationRepository: AuthorizationRepository,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val noteMarkPreferences: NoteMarkPreferences,
    private val nukeAllNotesUseCase: NukeAllNotesUseCase) : LogoutUseCase {
    override suspend fun execute(): Either<Unit, DataError> {
        val refreshToken = noteMarkPreferences.getRefreshToken()

        return if(refreshToken != null) {
            val logoutRequest = LogoutRequest(
                refreshToken = refreshToken
            )
            when(val result = authorizationRepository.logout(logoutRequest)) {
                is Left -> {
                    syncNoteScheduler.cancelAllSyncs()
                    noteMarkPreferences.deleteAllPreferences()
                    nukeAllNotesUseCase.execute()
                    Left(Unit)
                }
                is Right -> {
                    Right(result.right)
                }
            }
        }
        else {
            Right(DataError.Local.EMPTY)
        }
    }
}