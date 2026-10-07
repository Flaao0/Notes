package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNote(query)
    }
}