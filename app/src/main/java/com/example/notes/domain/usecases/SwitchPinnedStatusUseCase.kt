package com.example.notes.domain.usecases

import com.example.notes.domain.repository.NoteRepository

class SwitchPinnedStatusUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(id: Int) {
        repository.switchPinnedStatusUseCase(id)
    }
}