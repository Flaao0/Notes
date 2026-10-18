package com.flaao0.notes.data.repository

import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object TestNotesRepositoryImpl : NotesRepository {

    private val notesListFlow = MutableStateFlow<List<Note>>(listOf())



    override fun addNote(title: String, content: String) {
        notesListFlow.update {
            val note = Note(
                id = it.size,
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis(),
                isPinned = false
            )
            it + note
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesListFlow
    }

    override fun getNote(noteId: Int): Note {
        return notesListFlow.value.first { it.id == noteId }
    }

    override fun editNote(note: Note) {
        notesListFlow.update { oldList ->
            oldList.map {
                if (it.id == note.id) {
                    note
                } else {
                    it
                }
            }
        }
    }

    override fun deleteNote(noteId: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().apply {
                removeIf { it.id == noteId }
            }
        }
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesListFlow.map {
            it.filter {note ->
                note.title.contains(query) || note.content.contains(query)
            }
        }
    }

    override fun switchPinnedStatus(noteId: Int) {
        notesListFlow.update { oldList ->
            oldList.map {
                if (it.id == noteId) {
                    it.copy(isPinned = !it.isPinned)
                } else {
                    it
                }
            }
        }
    }
}