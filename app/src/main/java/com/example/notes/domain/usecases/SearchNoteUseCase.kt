package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase {

    operator fun invoke(query: String): Flow<List<Note>> {
        TODO()
    }
}