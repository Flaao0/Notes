package com.flaao0.notes.presentation.screens.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.data.repository.TestNotesRepositoryImpl
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.usecases.DeleteNoteUseCase
import com.flaao0.notes.domain.usecases.EditNoteUseCase
import com.flaao0.notes.domain.usecases.GetNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val noteId: Int
) : ViewModel() {

    private val repository = TestNotesRepositoryImpl

    private val editNoteUseCase = EditNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId)
                EditNoteState.Editing(note)
            }
        }
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is EditNoteCommand.InputContent -> {
                _state.update { oldState ->
                    if (oldState is EditNoteState.Editing) {
                        val newNote = oldState.note.copy(
                            content = command.content
                        )
                        oldState.copy(
                            note = newNote
                        )
                    } else {
                        oldState
                    }
                }
            }

            is EditNoteCommand.InputTitle -> {
                _state.update { oldState ->
                    if (oldState is EditNoteState.Editing) {
                        val newNote = oldState.note.copy(
                            title = command.title
                        )
                        oldState.copy(
                            note = newNote
                        )
                    } else {
                        oldState
                    }
                }
            }

            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { oldState ->
                        if (oldState is EditNoteState.Editing) {
                            val note = oldState.note
                            editNoteUseCase(note)
                            EditNoteState.Finished
                        } else {
                            oldState
                        }
                    }
                }
            }

            is EditNoteCommand.DeleteNote -> {
                viewModelScope.launch {
                    _state.update { oldState ->
                        if (oldState is EditNoteState.Editing) {
                            val note = oldState.note
                            deleteNoteUseCase(note.id)
                            EditNoteState.Finished
                        } else {
                            oldState
                        }
                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand {

    data class InputTitle(val title: String) : EditNoteCommand

    data class InputContent(val content: String) : EditNoteCommand

    data object DeleteNote: EditNoteCommand

    data object Save : EditNoteCommand

    data object Back : EditNoteCommand
}

sealed interface EditNoteState {

    data object Initial: EditNoteState

    data class Editing(
        val note: Note
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() = note.title.isNotBlank() && note.content.isNotBlank()
    }

    data object Finished : EditNoteState
}