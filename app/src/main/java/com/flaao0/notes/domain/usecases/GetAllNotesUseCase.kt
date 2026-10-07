package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(
    private val repository: NotesRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}