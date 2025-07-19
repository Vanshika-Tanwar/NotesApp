package com.example.notesapp.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesapp.Models.Notes
import com.example.notesapp.ui.theme.BlackColor
import com.example.notesapp.ui.theme.DarkGrey
import com.example.notesapp.ui.theme.LightGrey
import com.example.notesapp.ui.theme.RedColor
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun InsertNotesScreen(navHostController: NavHostController, id: String?) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val notesDB = db.collection("notes")

    val title = remember { mutableStateOf("") }
    val desc = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            notesDB.document(id.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Notes::class.java)
                title.value = singleData!!.title
                desc.value = singleData.desc
            }
        }
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                if (title.value.isEmpty() && desc.value.isEmpty()) {
                    Toast.makeText(context, "Enter valid data", Toast.LENGTH_SHORT).show()
                } else {

                    val notesId = if (id != "defaultId") {
                        id.toString()
                    } else {
                        notesDB.document().id
                    }

                    val notes = Notes(
                        id = notesId,
                        title = title.value,
                        desc = desc.value
                    )

                    notesDB.document(notesId).set(notes).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Note added successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            navHostController.popBackStack()

                        } else {
                            Toast.makeText(
                                context,
                                "Something went wrong...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            },
            contentColor = Color.White,
            containerColor = RedColor,
            shape = RoundedCornerShape(CornerSize(100.dp))
        ) {
            Icon(imageVector = Icons.Default.Done, "submit new note")
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
                    "Insert Note", style = TextStyle(
                        fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkGrey,
                        unfocusedContainerColor = DarkGrey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ), shape = RoundedCornerShape(corner = CornerSize(15.dp)), label = {
                        Text(
                            "Title", style = TextStyle(fontSize = 18.sp, color = LightGrey)
                        )
                    }, value = title.value, onValueChange = {
                        title.value = it
                    }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    textStyle = TextStyle(color = Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkGrey,
                        unfocusedContainerColor = DarkGrey,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    label = {
                        Text(
                            "Write your Note here...",
                            style = TextStyle(fontSize = 18.sp, color = LightGrey)
                        )
                    },
                    value = desc.value,
                    onValueChange = {
                        desc.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                )

            }
        }

    }
}