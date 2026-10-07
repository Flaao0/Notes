package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.repository.NotesRepository

class DeleteNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}