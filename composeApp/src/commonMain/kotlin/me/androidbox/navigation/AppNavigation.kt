package me.androidbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import me.androidbox.notes.presentation.note_details.EditNoteScreenRoot
import me.androidbox.notes.presentation.note_list.NoteListScreenRoot
import me.androidbox.settings.presentation.SettingsScreenRoot
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

        composable<NavGraph.NotesListScreen> {
            val username = it.toRoute<NavGraph.NotesListScreen>().username
            NoteListScreenRoot(
                username = username,
                onNavigateToEditNote = { noteId ->
                    navHostController.navigate(NavGraph.NoteDetailsScreen(noteId))
                    navHostController.navigate(NavGraph.NoteDetailsScreen(noteId))
                },
                onNavigateToSettings = {
                    navHostController.navigate(NavGraph.SettingsScreen)
                })
        }

        composable<NavGraph.NoteDetailsScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<NavGraph.NoteDetailsScreen>()
            EditNoteScreenRoot(
                noteId = args.noteId,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<NavGraph.SettingsScreen> {
            SettingsScreenRoot(
                onLogoutClicked = {
                    navHostController.navigate(route = NavGraph.AuthenticationGraph) {
                        this.popUpTo(navHostController.graph.id)
                    }
                }
            )
        }
    }
}