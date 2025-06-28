package me.androidbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.androidbox.notes.presentation.edit_note.EditNoteScreenRoot
import me.androidbox.notes.presentation.note_list.NoteListScreenRoot
import me.androidbox.startup.presentation.LandingScreen

@Composable
fun AppNavigation(
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = NavGraph.AuthenticationGraph
    ) {
        composable<NavGraph.LandingScreen> {
            LandingScreen(
                onGettingStartedClick = {
                    navHostController.navigate(NavGraph.AuthenticationGraph.RegisterScreen)
                },
                onLoginClick = {
                    navHostController.navigate(NavGraph.AuthenticationGraph.LoginScreen)
                }
            )
        }

        this.authenticationGraph(navController = navHostController)

        composable<NavGraph.NotesScreen> {
            val username = it.toRoute<NavGraph.NotesScreen>().username
            NoteListScreenRoot(
                username = username,
                onNavigateToEditNote = { noteId ->
                    navHostController.navigate(NavGraph.NoteEditScreen(noteId))
                })
        }

        composable<NavGraph.NoteEditScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<NavGraph.NoteEditScreen>()
            EditNoteScreenRoot(
                noteId = args.noteId,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}