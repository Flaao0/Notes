package com.flaao0.notes.domain.repository

import com.flaao0.notes.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun addNote(
        title: String,
        content: String,
        updatedAt: Long,
        isPinned: Boolean
    )

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNote(noteId: Int): Note

    suspend fun editNote(note: Note)

    suspend fun deleteNote(noteId: Int)

    suspend fun searchNote(query: String): Flow<List<Note>>

    suspend fun switchPinnedStatus(noteId: Int)
}