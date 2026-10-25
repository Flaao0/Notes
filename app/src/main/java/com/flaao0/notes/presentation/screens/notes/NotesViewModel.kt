@file:Suppress("OPT_IN_USAGE")

package com.flaao0.notes.presentation.screens.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.data.repository.NotesRepositoryImpl
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.usecases.GetAllNotesUseCase
import com.flaao0.notes.domain.usecases.SearchNoteUseCase
import com.flaao0.notes.domain.usecases.SwitchPinnedStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    context: Context
) : ViewModel() {

    private val repository = NotesRepositoryImpl.getInstance(context)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query.onEach {
                _state.update {
                    it.copy(query = query.value)
                }
            }.flatMapLatest {
                if (it.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNoteUseCase(query.value)
                }
            }.collect {
                val pinnedNotes = it.filter { note -> note.isPinned }
                val otherNotes = it.filter { note -> !note.isPinned }

                _state.update { notesState ->
                    notesState.copy(
                        pinnedNotes = pinnedNotes, otherNotes = otherNotes
                    )
                }
            }
        }
    }

    fun processCommand(command: NotesCommand) {
        viewModelScope.launch {
            when (command) {
                is NotesCommand.InputSearchQuery -> {
                    query.update { command.query.trim() }
                }

                is NotesCommand.SwitchPinnedStatus -> {
                    switchPinnedStatusUseCase(noteId = command.noteId)
                }
            }
        }

    }
}

sealed interface NotesCommand {

    data class InputSearchQuery(val query: String) : NotesCommand

    data class SwitchPinnedStatus(val noteId: Int) : NotesCommand

}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf(),
)