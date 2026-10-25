package com.flaao0.notes.presentation.screens.creation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.data.repository.NotesRepositoryImpl
import com.flaao0.notes.domain.usecases.AddNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateNoteViewModel(
    context: Context
) : ViewModel() {

    private val repository = NotesRepositoryImpl.getInstance(
        context
    )

    private val addNoteUseCase = AddNoteUseCase(repository)

    private val _state = MutableStateFlow<CreateNoteState>(CreateNoteState.Create())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            CreateNoteCommand.Back -> {
                _state.update { CreateNoteState.Finished }
            }

            is CreateNoteCommand.InputContent -> {
                _state.update { oldState ->
                    if (oldState is CreateNoteState.Create) {
                        oldState.copy(
                            content = command.content,
                            isSaveEnabled = oldState.title.isNotBlank() && command.content.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Create(content = command.content)
                    }
                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { oldState ->
                    if (oldState is CreateNoteState.Create) {
                        oldState.copy(
                            title = command.title,
                            isSaveEnabled = command.title.isNotBlank() && oldState.content.isNotBlank()
                        )
                    } else {
                        CreateNoteState.Create(title = command.title)
                    }
                }
            }

            CreateNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { oldState ->
                        if (oldState is CreateNoteState.Create) {
                            val title = oldState.title
                            val content = oldState.content
                            addNoteUseCase(title, content)
                            CreateNoteState.Finished
                        } else {
                            oldState
                        }
                    }
                }

            }
        }
    }
}

sealed interface CreateNoteCommand {

    data class InputTitle(val title: String) : CreateNoteCommand

    data class InputContent(val content: String) : CreateNoteCommand

    data object Save : CreateNoteCommand

    data object Back : CreateNoteCommand
}

sealed interface CreateNoteState {

    data class Create(
        val title: String = "",
        val content: String = "",
        val isSaveEnabled: Boolean = false
    ) : CreateNoteState

    data object Finished : CreateNoteState
}