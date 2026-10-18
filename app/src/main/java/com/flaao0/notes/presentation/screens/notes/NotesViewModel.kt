@file:Suppress("OPT_IN_USAGE")

package com.flaao0.notes.presentation.screens.notes

import androidx.lifecycle.ViewModel
import com.flaao0.notes.data.repository.TestNotesRepositoryImpl
import com.flaao0.notes.domain.usecases.AddNoteUseCase
import com.flaao0.notes.domain.usecases.DeleteNoteUseCase
import com.flaao0.notes.domain.usecases.EditNoteUseCase
import com.flaao0.notes.domain.usecases.GetAllNotesUseCase
import com.flaao0.notes.domain.usecases.GetNoteUseCase
import com.flaao0.notes.domain.usecases.SearchNoteUseCase
import com.flaao0.notes.domain.usecases.SwitchPinnedStatusUseCase
import androidx.lifecycle.viewModelScope
import com.flaao0.notes.domain.entity.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel: ViewModel() {

    private val repository = TestNotesRepositoryImpl

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        addNotes()

        viewModelScope.launch {
            query.flatMapLatest {
                if (it.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNoteUseCase(query.value)
                }
            }.collect {
                val pinnedNotes = it.filter {note -> note.isPinned }
                val otherNotes = it.filter {note -> !note.isPinned }

                _state.update {notesState ->
                    notesState.copy(
                        pinnedNotes = pinnedNotes,
                        otherNotes = otherNotes
                    )
                }
            }
        }
    }

    // TODO
    private fun addNotes() {
        repeat(50) {
            addNoteUseCase(title = "Title №$it", content = "Content №$it")
        }
    }

    fun processCommand(command: NotesCommand) {
        when (command) {
            is NotesCommand.DeleteNote -> {
                deleteNoteUseCase(noteId = command.noteId)
            }
            is NotesCommand.EditNote -> {
                val note = getNoteUseCase(command.note.id)
                editNoteUseCase(note = note.copy(title = "${note.title} editable"))
            }
            is NotesCommand.InputSearchQuery -> {
                query.update { command.query.trim() }
            }
            is NotesCommand.SwitchPinnedStatus -> {
                switchPinnedStatusUseCase(noteId = command.noteId)
            }
        }
    }
}

sealed interface NotesCommand {

    data class InputSearchQuery(val query: String): NotesCommand

    data class SwitchPinnedStatus(val noteId: Int): NotesCommand

    // Temp

    data class EditNote(val note: Note): NotesCommand

    data class DeleteNote(val noteId: Int): NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf(),
)