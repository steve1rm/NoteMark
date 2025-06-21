package me.androidbox.navigation

import kotlinx.serialization.Serializable

sealed interface NavGraph {

    @Serializable
    data object LandingScreen : NavGraph

    @Serializable
    data object AuthenticationGraph : NavGraph {
        @Serializable
        object LoginScreen : NavGraph
        @Serializable
        object RegisterScreen : NavGraph
    }

    @Serializable
    data object NotesScreen : NavGraph

    @Serializable
    data object NoteEditScreen : NavGraph
}