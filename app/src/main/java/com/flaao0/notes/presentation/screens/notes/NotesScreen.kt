package com.flaao0.notes.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flaao0.notes.domain.entity.Note
import com.flaao0.notes.presentation.ui.theme.OtherNotesColors
import com.flaao0.notes.presentation.ui.theme.PinnedNotesColors
import com.flaao0.notes.presentation.utils.DateFormatter

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier
            .padding(top = 48.dp),
    ) {
        item {
            Title(
                modifier = modifier
                    .padding(horizontal = 24.dp),
            )
        }

        item {
            Spacer(modifier.height(16.dp))
        }

        item {
            SearchBar(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                query = state.query,
            ) {
                viewModel.processCommand(NotesCommand.InputSearchQuery(it))
            }
        }

        item {
            Spacer(modifier.height(24.dp))
        }

        item {
            Subtitle(
                modifier = modifier
                    .padding(horizontal = 24.dp),
                text = "Закрепленные"
            )
        }

        item {
            Spacer(modifier.height(16.dp))
        }

        item {
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                itemsIndexed(
                    items = state.pinnedNotes,
                    key = {_, note -> note.id }
                ) { index, note ->
                    NoteCard(
                        modifier = modifier.widthIn(max = 160.dp),
                        note = note,
                        color = PinnedNotesColors[index % PinnedNotesColors.size],
                        onNoteClick = {
                            viewModel.processCommand(NotesCommand.SwitchPinnedStatus(note.id))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NotesCommand.DeleteNote(note.id))
                        },
                        onLongClick = {
                            viewModel.processCommand(NotesCommand.EditNote(note))
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier.height(24.dp))
        }

        item {
            Subtitle(
                modifier = modifier
                    .padding(horizontal = 24.dp),
                text = "Остальные"
            )
        }

        item {
            Spacer(modifier.height(16.dp))
        }

        itemsIndexed(
            items = state.otherNotes,
            key = {_, note -> note.id }
        ) {index, note ->
            NoteCard(
                modifier = modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                note = note,
                color = OtherNotesColors[index % OtherNotesColors.size],
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.SwitchPinnedStatus(note.id))
                },
                onDoubleClick = {
                    viewModel.processCommand(NotesCommand.DeleteNote(note.id))
                },
                onLongClick = {
                    viewModel.processCommand(NotesCommand.EditNote(note))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "Заметки",
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
        modifier = modifier
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                RoundedCornerShape(10.dp)
            ),
        value = query,
        onValueChange = onQueryChanged,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
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
                contentDescription = "Поиск",
                tint = MaterialTheme.colorScheme.onSurface
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
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier.height(8.dp))

        Text(
            text = DateFormatter.formatDateToString(note.updatedAt),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier.height(24.dp))

        Text(
            text = note.content,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}