package com.example.cpcontactkeeper.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpcontactkeeper.data.model.Contact
import com.example.cpcontactkeeper.ui.screen.CONSTANTS.ADD_SCREEN
import com.example.cpcontactkeeper.viewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: ContactViewModel) {

    val contacts by viewModel.contacts.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contact Keeper",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF45C5FF))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ADD_SCREEN)
                },
                containerColor = Color(0xFF45C5FF),
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF2F7FA))
                .fillMaxSize()
        ) {
            items(contacts) { contact ->
                ContactCard(navController, contact)
            }
        }
    }
}

@Composable
fun ContactCard(navController: NavHostController, contact: Contact) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape)
            .clickable {
                navController.navigate(CONSTANTS.detailScreenRoute(contact.id))
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(model = contact.profileImage),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    color = Color(0xFF45C5FF),
                    text = contact.name,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = contact.phoneNumber.firstOrNull() ?: "No phone number",
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(NavHostController(LocalContext.current), ContactViewModel(LocalContext.current))
}