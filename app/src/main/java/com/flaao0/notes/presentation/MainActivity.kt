package com.flaao0.notes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.flaao0.notes.presentation.screens.creation.CreateNoteScreen
import com.flaao0.notes.presentation.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "on note click $it")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "on add note click")
//                    }
//                )

                CreateNoteScreen() {
                    Log.d("CreateNoteScreen", "finished")
                }
            }
        }
    }
}
