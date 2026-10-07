package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(note: Note) {
        repository.addNote(note)
    }
}