package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNote(query)
    }
}