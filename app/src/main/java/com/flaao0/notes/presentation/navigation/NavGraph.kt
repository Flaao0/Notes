package com.flaao0.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flaao0.notes.presentation.screens.creation.CreateNoteScreen
import com.flaao0.notes.presentation.screens.editing.EditNoteScreen
import com.flaao0.notes.presentation.screens.notes.NotesScreen


@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ) {
        composable(Screen.Notes.route) {
            NotesScreen(
                onNoteClick = {
                    navController.navigate(Screen.EditNote.route)
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote.route)
                }
            )
        }

        composable(Screen.CreateNote.route) {
            CreateNoteScreen() {
                navController.popBackStack()
            }
        }

        composable(Screen.EditNote.route) {
            EditNoteScreen(
                noteId = 5
            ) {
                navController.popBackStack()
            }
        }
    }
}

sealed class Screen(val route: String) {

    data object Notes : Screen("notes")

    data object CreateNote : Screen("create_note")

    data object EditNote : Screen("edit_note")
}