package me.androidbox.notes.data.mappers

import me.androidbox.core.presentation.utils.toEpochMilliSeconds
import me.androidbox.core.presentation.utils.toIsoFormat
import me.androidbox.notes.data.models.NoteItemDto
import me.androidbox.notes.data.models.NoteItemEntity
import me.androidbox.notes.domain.model.NoteItem

fun NoteItem.toNoteItemDto(): NoteItemDto {
    return NoteItemDto(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt.toIsoFormat(),
        lastEditedAt = this.lastEditedAt.toIsoFormat()
    )
}

fun NoteItemDto.toNoteItem(): NoteItem {
    return NoteItem(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt.toEpochMilliSeconds(),
        lastEditedAt = this.lastEditedAt.toEpochMilliSeconds()
    )
}

fun NoteItem.toNoteItemEntity(): NoteItemEntity {
    return NoteItemEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}

fun NoteItemEntity.toNoteItem(): NoteItem {
    return NoteItem(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}



