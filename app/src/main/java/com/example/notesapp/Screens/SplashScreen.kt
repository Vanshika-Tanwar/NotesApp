package com.example.notesapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notesapp.Navigation.NotesNavigationItem
import com.example.notesapp.R
import com.example.notesapp.ui.theme.BlackColor
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navHostController: NavHostController) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = BlackColor), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.size(150.dp)
            )
        }
    }
    LaunchedEffect(Unit) {
        delay(2500)
        navHostController.navigate(NotesNavigationItem.NotesScreen.route) {
            popUpTo(NotesNavigationItem.SplashScreen.route) {
                inclusive = true
            }
        }
    }
}