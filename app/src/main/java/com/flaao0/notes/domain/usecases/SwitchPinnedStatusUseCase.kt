package com.flaao0.notes.domain.usecases

import com.flaao0.notes.domain.repository.NotesRepository
import javax.inject.Inject

class SwitchPinnedStatusUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.switchPinnedStatus(noteId)
    }
}