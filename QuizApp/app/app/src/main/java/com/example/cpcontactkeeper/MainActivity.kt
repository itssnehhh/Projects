package com.example.cpcontactkeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.example.cpcontactkeeper.ui.screen.MainScreen
import com.example.cpcontactkeeper.viewModel.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel = ContactViewModel(LocalContext.current)
           MainScreen(viewModel)
        }
    }
}
