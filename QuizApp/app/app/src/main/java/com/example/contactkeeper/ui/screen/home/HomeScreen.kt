package com.example.contactkeeper.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.contactkeeper.R
import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.components.ContactItem
import com.example.contactkeeper.ui.screen.CONSTANTS.ADD_SCREEN
import com.example.contactkeeper.ui.screen.CONSTANTS.DETAIL_SCREEN
import com.example.contactkeeper.ui.theme.CustomBlue
import com.example.contactkeeper.ui.theme.CustomGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HomeViewModel>()
    val contacts by viewModel.contacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getContactList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(CustomBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("$ADD_SCREEN/${"0"}") },
                containerColor = CustomBlue,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) { paddingValues ->

        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(CustomGrey)
                    .fillMaxSize()
            ) {
                items(contacts) { contact ->
                    ContactItem(
                        contact = contact,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
        if (contacts.isEmpty()&&!isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.no_data_added),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ContactCard(navController: NavHostController, contact: Contact) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape)
            .clickable { navController.navigate("$DETAIL_SCREEN/${contact.id}") },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(model = contact.profilePicture),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray, CircleShape)
                    .clip(CircleShape)
                    .size(60.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    color = CustomBlue,
                    text = contact.name,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = contact.phoneNumber.firstOrNull()
                        ?: stringResource(R.string.no_phone_number),
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
    HomeScreen(NavHostController(LocalContext.current))
}