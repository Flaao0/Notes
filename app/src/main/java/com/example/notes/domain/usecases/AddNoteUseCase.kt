package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(note: Note) {
        repository.addNote(note)
    }
}