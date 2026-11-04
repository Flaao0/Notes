package com.flaao0.notes.data.repository

import com.flaao0.notes.data.local.dao.NotesDao
import com.flaao0.notes.data.local.entity.NoteDbModel
import com.flaao0.notes.data.mappers.toDbModel
import com.flaao0.notes.data.mappers.toEntity
import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(
    private val notesDao: NotesDao,
): NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        updatedAt: Long,
        isPinned: Boolean
    ) {
        val note = Note(
            0, title, content, updatedAt, isPinned
        )
        val noteDbModel = note.toDbModel()
        notesDao.addNote(
            noteDbModel
        )
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.map {noteDbModel ->
                noteDbModel.toEntity()
            }
        }
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesDao.searchNote(query).map {
            it.map { noteDbModel ->
                noteDbModel.toEntity()
            }
        }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    companion object {
        private var instance: NotesRepositoryImpl? = null
        private val lock = Any()

        fun getInstance(notesDao: NotesDao): NotesRepositoryImpl {
            instance?.let { return it }

            synchronized(lock) {
                instance?.let { return it }

                return NotesRepositoryImpl(notesDao).also {
                    instance = it
                }
            }
        }
    }
}