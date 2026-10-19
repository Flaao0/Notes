package com.flaao0.notes.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flaao0.notes.domain.entity.Note

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Title(text = "Заметки")
        }

        item {
            SearchBar(
                modifier = modifier.fillMaxWidth(),
                query = state.query,
            ) {
                viewModel.processCommand(NotesCommand.InputSearchQuery(it))
            }
        }

        item {
            Subtitle(text = "Закрепленные")
        }

        item {
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.pinnedNotes,
                    key = { it.id }
                ) {
                    NoteCard(
                        note = it,
                        color = Color.Yellow,
                        onNoteClick = {
                            viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NotesCommand.DeleteNote(it.id))
                        },
                        onLongClick = {
                            viewModel.processCommand(NotesCommand.EditNote(it))
                        }
                    )
                }
            }
        }

        item {
            Subtitle(text = "Остальные")
        }

        items(
            items = state.otherNotes,
            key = { it.id }
        ) {
            NoteCard(
                modifier = modifier.fillMaxWidth(),
                note = it,
                color = Color.Green,
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                },
                onDoubleClick = {
                    viewModel.processCommand(NotesCommand.DeleteNote(it.id))
                },
                onLongClick = {
                    viewModel.processCommand(NotesCommand.EditNote(it))
                }
            )
        }

    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Поиск...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск"
            )
        },
        shape = RoundedCornerShape(10.dp),

        )
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    color: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                },
                onDoubleClick = {
                    onDoubleClick(note)
                }
            )
    ) {
        Text(
            text = note.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
        Text(
            text = note.updatedAt.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = note.content,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}