package me.androidbox.notes.presentation.note_details.model

import notemark.composeapp.generated.resources.Res
import notemark.composeapp.generated.resources.ic_book
import notemark.composeapp.generated.resources.ic_edit
import org.jetbrains.compose.resources.DrawableResource

enum class NoteDetailActionButtons(
    val iconDrawable: DrawableResource,
    val noteDetailsMode: NoteDetailsMode
) {
    EditorActionButton(
        iconDrawable = Res.drawable.ic_edit,
        noteDetailsMode = NoteDetailsMode.EDIT_MODE
    ),
    ReaderActionButton(
        iconDrawable = Res.drawable.ic_book,
        noteDetailsMode = NoteDetailsMode.READER_MODE
    )
}