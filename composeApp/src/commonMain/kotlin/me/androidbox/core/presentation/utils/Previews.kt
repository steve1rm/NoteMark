package me.androidbox.core.presentation.utils

import androidx.compose.ui.text.buildAnnotatedString
import me.androidbox.getCurrentMillis
import me.androidbox.notes.domain.model.NoteItem
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object Previews {
    val noteItem = NoteItem(
        id = Uuid.random().toString(),
        title = "Item ${Random.nextInt(0, 999)}",
        content = buildAnnotatedString {
            repeat((10..30).random()) {
                append("Content $it")
            }
        }.toString(),
        createdAt = 0,
        lastEditedAt = 0
    )

    val noteItemList = (0..100).map {
        NoteItem(
            id = Uuid.random().toString(),
            title = "Item ${Random.nextInt(0, 999)}",
            content = buildAnnotatedString {
                repeat((10..30).random()) {
                    append("Content $it")
                }
            }.toString(),
            createdAt = getCurrentMillis() - 31_536_000_000L,
            lastEditedAt = getCurrentMillis()
        )
        // - 31_536_000_000L /* 1 year in millis xD */
    }

}