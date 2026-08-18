package com.example.notes.data

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class TestNoteRepositoryImpl : NoteRepository {



    override fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(id: Int) {
        TODO("Not yet implemented")
    }

    override fun editNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun getAllNotes(): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun getNote(id: Int): Note {
        TODO("Not yet implemented")
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun switchPinnedStatusUseCase(id: Int) {
        TODO("Not yet implemented")
    }
}