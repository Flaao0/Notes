package com.example.notes.domain.repository

import com.example.notes.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun addNote(
        title: String, content: String
    )

    fun deleteNote(id: Int)
    fun editNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
    fun getNote(id: Int): Note
    fun searchNote(query: String): Flow<List<Note>>
    fun switchPinnedStatusUseCase(id: Int)
}