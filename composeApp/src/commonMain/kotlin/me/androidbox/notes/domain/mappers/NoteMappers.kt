package me.androidbox.notes.domain.mappers

import me.androidbox.notes.data.models.NoteItemDto
import me.androidbox.notes.data.models.NoteItemEntity
import me.androidbox.notes.domain.model.NoteItem

fun NoteItem.toNoteItemDto(): NoteItemDto {
    return NoteItemDto(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}

fun NoteItemDto.toNoteItem(): NoteItem {
    return NoteItem(
        id = this.id,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}

fun NoteItem.toNoteItemEntity(): NoteItemEntity {
    return NoteItemEntity(
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}

fun NoteItemEntity.toNoteItem(): NoteItem {
    return NoteItem(
        id = this.id.toString(),
        title = this.title,
        content = this.content,
        createdAt = this.createdAt,
        lastEditedAt = this.lastEditedAt
    )
}



