package com.flaao0.notes.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.domain.entity.ContentItem
import com.flaao0.notes.domain.usecases.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

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
                        val newContent = oldState.content
                            .mapIndexed { index, item ->
                                if (index == command.index && item is ContentItem.Text) {
                                    item.copy(text = command.content)
                                } else {
                                    item
                                }
                            }
                        oldState.copy(content = newContent)
                    } else {
                        oldState
                    }
                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { oldState ->
                    if (oldState is CreateNoteState.Create) {
                        oldState.copy(
                            title = command.title,
                        )
                    } else {
                        oldState
                    }
                }
            }

            CreateNoteCommand.Save ->
                viewModelScope.launch {
                    _state.update { oldState ->
                        if (oldState is CreateNoteState.Create) {
                            val title = oldState.title
                            val content = oldState.content.filter {
                                it !is ContentItem.Text || it.text.isNotBlank()
                            }
                            addNoteUseCase(title, content)
                            CreateNoteState.Finished
                        } else {
                            oldState
                        }
                    }
                }

            is CreateNoteCommand.AddImage -> {
                viewModelScope.launch {
                    _state.update { oldState ->
                        if (oldState is CreateNoteState.Create) {
                            val newItems = oldState.content.toMutableList()
                            val lastItem = newItems.last()
                            if (lastItem is ContentItem.Text && lastItem.text.isBlank()) {
                                newItems.removeAt(newItems.lastIndex)
                            }
                            newItems.add(ContentItem.Image(command.uri))
                            newItems.add(ContentItem.Text(""))
                            oldState.copy(content = newItems)
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

    data class InputContent(val content: String, val index: Int) : CreateNoteCommand

    data class AddImage(val uri: String) : CreateNoteCommand

    data object Save : CreateNoteCommand

    data object Back : CreateNoteCommand
}

sealed interface CreateNoteState {

    data class Create(
        val title: String = "",
        val content: List<ContentItem> = listOf(ContentItem.Text(""))
    ) : CreateNoteState {
        val isSaveEnabled: Boolean
            get() {
                return when {
                    title.isBlank() -> false
                    content.isEmpty() -> false
                    else -> {
                        content.any {
                            it !is ContentItem.Text || it.text.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : CreateNoteState
}