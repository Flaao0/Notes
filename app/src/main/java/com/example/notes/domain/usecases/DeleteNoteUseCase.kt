package com.example.notes.domain.usecases

import com.example.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(id: Int) {
        repository.deleteNote(id)
    }
}