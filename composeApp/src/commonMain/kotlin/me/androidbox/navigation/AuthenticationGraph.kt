package me.androidbox.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.androidbox.authentication.login.presentation.LoginScreen
import me.androidbox.authentication.register.presentation.RegisterScreen

fun NavGraphBuilder.authenticationGraph(navController: NavController) {
    this.navigation<NavGraph.AuthenticationGraph>(
        startDestination = NavGraph.AuthenticationGraph.LoginScreen
    ) {
        composable<NavGraph.AuthenticationGraph.LoginScreen> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(NavGraph.AuthenticationGraph.RegisterScreen)
                }
            )
        }
        composable<NavGraph.AuthenticationGraph.RegisterScreen> {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(NavGraph.AuthenticationGraph.LoginScreen)
                }
            )
        }
    }
}