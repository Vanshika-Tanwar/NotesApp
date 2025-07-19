package com.example.notesapp.Navigation

sealed class NotesNavigationItem(val route : String) {
    object SplashScreen : NotesNavigationItem("splash")
    object NotesScreen : NotesNavigationItem("home")
    object InsertNotesScreen : NotesNavigationItem("createNotes")
}