package com.flaao0.notes.presentation.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flaao0.notes.domain.entity.Note

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier, viewModel: NotesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(top = 48.dp)
    ) {

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                items(
                    state.pinnedNotes,
                    key = {it.id}
                ) {note ->
                    NoteCard(note = note) {
                        viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                    }
                }
            }
        }

        items(
            state.otherNotes,
            key = {it.id}
        ) {note ->
            NoteCard(note = note) {
                viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
            }
        }
    }
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier, note: Note, onNoteClick: (Note) -> Unit
) {
    Text(
        modifier = Modifier.clickable {
            onNoteClick(note)
        }, text = note.title, fontSize = 24.sp
    )
}