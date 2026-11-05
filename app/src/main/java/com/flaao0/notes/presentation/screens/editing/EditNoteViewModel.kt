package com.flaao0.notes.presentation.screens.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.domain.usecases.DeleteNoteUseCase
import com.flaao0.notes.domain.usecases.EditNoteUseCase
import com.flaao0.notes.domain.usecases.GetNoteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditNoteViewModel.Factory::class)
class EditNoteViewModel @AssistedInject constructor(
    @Assisted("noteId") private val noteId: Int,
    private val editNoteUseCase: EditNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase
) : ViewModel() {

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

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("noteId") noteId: Int): EditNoteViewModel
    }

    fun processCommand(command: EditNoteCommand) {
        when (command) {
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is EditNoteCommand.InputContent -> {

                _state.update { oldState ->
                    if (oldState is EditNoteState.Editing) {
                        val newContent = oldState.note.content
                            .mapIndexed { index, item ->
                                if (index == command.index && item is ContentItem.Text) {
                                    item.copy(text = command.content)
                                } else {
                                    item
                                }
                            }
                        val newNote: Note = oldState.note.copy(content = newContent)
                        oldState.copy(note = newNote)
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
                            editNoteUseCase(oldState.note)
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
                            deleteNoteUseCase(oldState.note.id)
                            EditNoteState.Finished
                        } else {
                            oldState
                        }
                    }
                }
            }

            is EditNoteCommand.DeleteImage -> {
                _state.update { oldState ->
                    if (oldState is EditNoteState.Editing) {
                        val newContent = oldState.note.content.toMutableList().apply {
                            remove(ContentItem.Image(command.uri))
                            add(ContentItem.Text(""))
                        }.toList()
                        val newNote = oldState.note.copy(
                            content = newContent
                        )
                        oldState.copy(note = newNote)
                    } else {
                        oldState
                    }
                }
            }

            is EditNoteCommand.AddImage -> {
                _state.update { oldState ->
                    if (oldState is EditNoteState.Editing) {
                        val newItems = oldState.note.content.toMutableList()
                        val lastItem = newItems.lastOrNull()
                        if (lastItem is ContentItem.Text && lastItem.text.isBlank()) {
                            newItems.removeAt(newItems.lastIndex)
                        }
                        newItems.add(ContentItem.Image(command.uri))
                        newItems.add(ContentItem.Text(""))
                        val newNote = oldState.note.copy(
                            content = newItems.toList()
                        )
                        oldState.copy(note = newNote)
                    } else {
                        oldState
                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand {

    data class InputTitle(val title: String) : EditNoteCommand

    data class InputContent(val content: String, val index: Int) : EditNoteCommand

    data class DeleteImage(val uri: String): EditNoteCommand

    data class AddImage(val uri: String): EditNoteCommand

    data object DeleteNote : EditNoteCommand

    data object Save : EditNoteCommand

    data object Back : EditNoteCommand
}

sealed interface EditNoteState {

    data object Initial : EditNoteState

    data class Editing(
        val note: Note
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() {
                return when {
                    note.title.isBlank() -> false
                    note.content.isEmpty() -> false
                    else -> {
                        note.content.any {
                            it !is ContentItem.Text || it.text.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : EditNoteState
}