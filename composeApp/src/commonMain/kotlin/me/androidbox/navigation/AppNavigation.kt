package me.androidbox.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.androidbox.startup.presentation.LandingScreen

@Composable
fun AppNavigation(
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = NavGraph.LandingScreen
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
    }
}