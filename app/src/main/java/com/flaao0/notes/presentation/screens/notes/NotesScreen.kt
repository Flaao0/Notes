package com.flaao0.notes.presentation.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier, viewModel: NotesViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(top = 48.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            state.pinnedNotes.forEach {
                Text(
                    modifier = Modifier.clickable {
                            viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                        }, text = "Title: ${it.title}"
                )
            }
        }
        state.otherNotes.forEach {
            Text(
                modifier = Modifier.clickable {
                        viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                    }, text = "Title: ${it.title}"
            )
        }
    }
}