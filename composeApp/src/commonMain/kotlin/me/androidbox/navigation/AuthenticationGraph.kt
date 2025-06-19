package me.androidbox.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.androidbox.authentication.login.presentation.LoginScreenRoot
import me.androidbox.authentication.register.presentation.RegisterScreenRoot

fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    this.navigation<NavGraph.AuthenticationGraph>(
        startDestination = NavGraph.AuthenticationGraph.LoginScreen
    ) {
        composable<NavGraph.AuthenticationGraph.LoginScreen> {
            LoginScreenRoot(
                onNavigateToRegister = {
                    navController.navigate(NavGraph.AuthenticationGraph.RegisterScreen)
                },
                onNavigateToBlankScreen = {
                    navController.navigate(NavGraph.NotesScreen, {
                        popUpTo(0) { // Pop everything from back stack, e.g (Startup, login, register)
                            inclusive = true
                        }
                    })
                }
            )
        }
        composable<NavGraph.AuthenticationGraph.RegisterScreen> {
            RegisterScreenRoot(
                onNavigateToLogin = {
                    navController.navigate(NavGraph.AuthenticationGraph.LoginScreen)
                }
            )
        }
    }
}