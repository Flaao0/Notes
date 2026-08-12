package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class EditNoteUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(note: Note) {
        repository.editNote(note)
    }
}