package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.repository.NotesRepository

class SwitchPinnedStatusUseCase(
    private val repository: NotesRepository
) {
    operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}