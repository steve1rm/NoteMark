package me.androidbox.notes.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProfilePictureUseCase {
    suspend operator fun invoke(username: String): String {
        return withContext(Dispatchers.Default) {
            val words = username.uppercase().split(" ")
            if (words.size == 1) {
                return@withContext words[0].take(2)
            }
            if (words.size == 2) {
                return@withContext "${words[0].take(1)}${words[1].take(1)}"
            }
            "${words[0].take(1)}${words[words.lastIndex].take(1)}"
        }
    }
}