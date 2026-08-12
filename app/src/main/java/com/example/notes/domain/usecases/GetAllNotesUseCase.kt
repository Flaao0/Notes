package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(

) {

    operator fun invoke(): Flow<List<Note>> {
        TODO()
    }
}