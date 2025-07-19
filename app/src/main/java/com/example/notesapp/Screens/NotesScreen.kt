package com.example.notesapp.Screens

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.notesapp.Models.Notes
import com.example.notesapp.Navigation.NotesNavigationItem
import com.example.notesapp.ui.theme.BlackColor
import com.example.notesapp.ui.theme.DarkGrey
import com.example.notesapp.ui.theme.LightGrey
import com.example.notesapp.ui.theme.RedColor
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NotesScreen(navHostController: NavHostController) {

    /* //DUMMY DATA
    val notesList = listOf(
        Notes(
            title = "Meeting with Team",
            desc = "Discuss project roadmap and deliverables for next sprint."
        ),
        Notes(
            title = "Grocery List",
            desc = "Buy milk, eggs, bread, and fresh vegetables."
        ),
        Notes(
            title = "Workout Plan",
            desc = "Leg day exercises: squats, lunges, leg press, and hamstring curls."
        ),
        Notes(
            title = "App Feature Ideas",
            desc = "Consider adding a dark mode toggle and push notification feature."
        ),
        Notes(
            title = "Book to Read",
            desc = "Start reading 'Atomic Habits' by James Clear this weekend."
        ),
        Notes(
            title = "Client Follow-up",
            desc = "Send a follow-up email to the client regarding the proposal."
        ),
        Notes(
            title = "Weekend Getaway",
            desc = "Plan a trip to the mountains and book an Airbnb by Friday."
        ),
        Notes(
            title = "Code Refactoring",
            desc = "Refactor the main activity to improve performance and readability."
        ),
        Notes(
            title = "Birthday Reminder",
            desc = "Remember to send a birthday card to Sarah on October 15."
        ),
        Notes(
            title = "Budget Planning",
            desc = "Prepare next month's budget and analyze spending on utilities."
        ),
        Notes(
            title = "UI Design Sketch",
            desc = "Design a new layout for the login screen in Figma."
        ),
        Notes(
            title = "Movie Night",
            desc = "Watch 'Inception' or 'The Dark Knight' with friends on Saturday."
        ),
        Notes(
            title = "App Testing",
            desc = "Run integration tests on the payment gateway for the latest release."
        ),
        Notes(
            title = "Dinner Recipe",
            desc = "Try a new recipe: Creamy garlic chicken with mashed potatoes."
        ),
        Notes(
            title = "Self-Care Routine",
            desc = "Meditation, yoga, and journaling for 30 minutes before bed."
        )
    )*/

    val db = FirebaseFirestore.getInstance()
    val notesDB = db.collection("notes")

    val notesList = remember {
        mutableStateListOf<Notes>()
    }
    val dataValue = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        notesDB.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.toObjects(Notes::class.java)
                notesList.clear()
                notesList.addAll(data!!)
                dataValue.value = true;
            } else {
                dataValue.value = false;
            }
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            contentColor = Color.White,
            containerColor = RedColor,
            shape = RoundedCornerShape(CornerSize(100.dp)),
            onClick = {

                navHostController.navigate(NotesNavigationItem.InsertNotesScreen.route+"/defaultId")

            }) {
            Icon(imageVector = Icons.Default.Add, "add new note")
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(BlackColor)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    "Create Notes Here!\nVT", style = TextStyle(
                        fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
                if (dataValue.value) {
                    LazyColumn {
                        items(notesList) { notes ->
                            ListItems(notes, notesDB, navHostController)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItems(notes: Notes, notesDB: CollectionReference, navHostController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(color = DarkGrey)
    ) {

        DropdownMenu(
            modifier = Modifier.background(color = Color.White),
            properties = PopupProperties(clippingEnabled = true),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Update", style = TextStyle(color = DarkGrey)) },
                onClick = {
                    navHostController.navigate(NotesNavigationItem.InsertNotesScreen.route+"/${notes.id}")

                })
            DropdownMenuItem(
                text = { Text("Delete", style = TextStyle(color = DarkGrey)) },
                onClick = {
                    val alertDialog = AlertDialog.Builder(context)

                    alertDialog.setMessage("Are your sure you want to delete this note?")

                    alertDialog.setPositiveButton("Yes") { dialog, which ->

                            notesDB.document(notes.id).delete()
                            dialog?.dismiss()
                            expanded = false


                    }
                    alertDialog.setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        dialog?.dismiss()
                        expanded = false
                    }

                    alertDialog.show()
                })
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            "",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable {
                    expanded = true
                }
        )
        Column(modifier = Modifier.padding(15.dp)) {
            Text(text = notes.title, style = TextStyle(color = Color.White))
            Text(text = notes.desc, style = TextStyle(color = LightGrey))
        }
    }

}