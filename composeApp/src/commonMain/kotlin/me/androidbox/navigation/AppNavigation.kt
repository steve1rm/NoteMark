package me.androidbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.androidbox.notes.presentation.EditNoteScreenRoot
import me.androidbox.notes.presentation.NoteListScreenRoot
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
            NoteListScreenRoot(onNavigateToEditNote = {
                navHostController.navigate(NavGraph.NoteEditScreen)
            })
        }

        composable<NavGraph.NoteEditScreen> {
            EditNoteScreenRoot()
        }
    }
}