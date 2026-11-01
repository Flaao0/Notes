package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.repository.NotesRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(
        title: String,
        content: List<ContentItem>
    ) {
        repository.addNote(
            title = title,
            content = content,
            updatedAt = System.currentTimeMillis(),
            isPinned = false
        )
    }
}