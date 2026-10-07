package com.flaao0.notes.domain.repository

import com.flaao0.notes.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    fun addNote(note: Note)

    fun getAllNotes(): Flow<List<Note>>

    fun getNote(noteId: Int): Note

    fun editNote(note: Note): Note

    fun deleteNote(noteId: Int)

    fun searchNote(query: String): Flow<List<Note>>

    fun switchPinnedStatus(noteId: Int)
}