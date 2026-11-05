package com.flaao0.notes.data.repository

import com.flaao0.notes.data.InternalFileManager
import com.flaao0.notes.data.local.dao.NotesDao
import com.flaao0.notes.data.mappers.toDbModel
import com.flaao0.notes.data.mappers.toEntity
import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(
    private val notesDao: NotesDao,
    private val internalFileManager: InternalFileManager
): NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        updatedAt: Long,
        isPinned: Boolean
    ) {
        val note = Note(
            0, title, content.processForStorage(), updatedAt, isPinned
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
        val oldNote = getNote(note.id)

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>()
        val newUrls = note.content.filterIsInstance<ContentItem.Image>()
        val removedUrls = oldUrls - newUrls

        removedUrls.forEach {
            internalFileManager.deleteImage(it.url)
        }

        val processedContent = note.content.processForStorage()
        val processedNote = note.copy(content = processedContent)

        notesDao.addNote(processedNote.toDbModel())
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = getNote(noteId)
        notesDao.deleteNote(noteId)

        note.content.filterIsInstance<ContentItem.Image>().map { it.url }.forEach {
            internalFileManager.deleteImage(it)
        }
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

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when (contentItem) {
                is ContentItem.Image -> {
                    if (internalFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val path = internalFileManager.copyImageToInternalStorage(contentItem.url)
                        ContentItem.Image(path)
                    }
                }
                is ContentItem.Text -> contentItem
            }
        }
    }

    companion object {
        private var instance: NotesRepositoryImpl? = null
        private val lock = Any()

        fun getInstance(notesDao: NotesDao, internalFileManager: InternalFileManager): NotesRepositoryImpl {
            instance?.let { return it }

            synchronized(lock) {
                instance?.let { return it }

                return NotesRepositoryImpl(notesDao, internalFileManager).also {
                    instance = it
                }
            }
        }
    }
}