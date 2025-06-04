package me.androidbox.navigation

import kotlinx.serialization.Serializable

sealed interface NavGraph {

    @Serializable
    object LandingScreen : NavGraph

    @Serializable
    data object AuthenticationGraph : NavGraph {
        @Serializable
        object LoginScreen : NavGraph
        @Serializable
        object RegisterScreen : NavGraph
    }

}