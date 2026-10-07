package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository

class GetNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(noteId: Int): Note {
        return repository.getNote(noteId)
    }
}