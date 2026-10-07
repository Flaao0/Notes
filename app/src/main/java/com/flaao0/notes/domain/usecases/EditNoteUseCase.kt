package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository

class EditNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(note: Note): Note {
        return repository.editNote(note)
    }
}