package com.flaao0.notes.data.mappers

import com.flaao0.notes.data.entity.NoteDbModel
import com.flaao0.notes.domain.entity.Note

fun Note.toDbModel(): NoteDbModel {
    return NoteDbModel(
        id, title, content, updatedAt, isPinned
    )
}

fun NoteDbModel.toEntity(): Note {
    return Note(
        id, title, content, updatedAt, isPinned
    )
}