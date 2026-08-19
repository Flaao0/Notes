package com.example.notes.presentation.screens.notes

import androidx.lifecycle.ViewModel
import com.example.notes.data.TestNoteRepositoryImpl
import com.example.notes.domain.entity.Note
import com.example.notes.domain.usecases.AddNoteUseCase
import com.example.notes.domain.usecases.DeleteNoteUseCase
import com.example.notes.domain.usecases.EditNoteUseCase
import com.example.notes.domain.usecases.GetAllNotesUseCase
import com.example.notes.domain.usecases.GetNoteUseCase
import com.example.notes.domain.usecases.SearchNoteUseCase
import com.example.notes.domain.usecases.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel: ViewModel() {

    private val repository = TestNoteRepositoryImpl

    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)

    private val query = MutableStateFlow("")

    private val _state = MutableStateFlow(NotesScreenState())
    val state
        get() = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        query
            .onEach { query ->
                _state.update { it.copy(query = query) }
            }
            .flatMapLatest {
                if (it.isBlank()) {
                    getAllNotesUseCase()
                } else {
                    searchNoteUseCase(it)
                }
            }
            .onEach { listNotes ->
                val pinnedNotes = listNotes.filter { it.isPinned }
                val otherNotes = listNotes.filter { !it.isPinned }

                _state.update {
                    it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes)
                }
            }
            .launchIn(scope)
    }

    fun processCommand(command: NotesCommand) {
        when (command) {
            is NotesCommand.Delete -> {
                deleteNoteUseCase(command.id)
            }
            is NotesCommand.EditNote -> {
                val title = command.note.title
                editNoteUseCase(command.note.copy(title = "$title edited"))
            }
            is NotesCommand.InputSearchQuery -> {
                query.update {
                    command.query.trim()
                }
            }
            is NotesCommand.SwitchPinnedStatus -> {
                switchPinnedStatusUseCase(command.id)
            }
        }
    }
}

sealed interface NotesCommand {

    data class InputSearchQuery(val query: String): NotesCommand
    data class SwitchPinnedStatus(val id: Int): NotesCommand

    // Temp
    data class EditNote(val note: Note): NotesCommand
    data class Delete(val id: Int): NotesCommand
}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)