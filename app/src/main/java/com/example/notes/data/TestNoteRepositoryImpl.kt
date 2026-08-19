package com.example.notes.data

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object TestNoteRepositoryImpl : NoteRepository {

    val notesListFlow = MutableStateFlow<List<Note>>(listOf())

    override fun addNote(note: Note) {
        notesListFlow.update {
            it + note
        }
    }

    override fun deleteNote(id: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().apply {
                removeIf {
                    it.id == id
                }
            }
        }
    }

    override fun editNote(note: Note) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().map {
                if (it.id == note.id) {
                    note
                } else {
                    it
                }
            }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesListFlow.asStateFlow()
    }

    override fun getNote(id: Int): Note {
        return notesListFlow.value.first {
            it.id == id
        }
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesListFlow.map {oldList ->
            oldList.filter {
                it.title.contains(query) || it.content.contains(query)
            }
        }
    }

    override fun switchPinnedStatusUseCase(id: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().map {
                if (it.id == id) {
                    it.copy(isPinned = !it.isPinned)
                } else {
                    it
                }
            }
        }
    }
}