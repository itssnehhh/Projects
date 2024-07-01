package com.example.cpcontactkeeper.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cpcontactkeeper.R
import com.example.cpcontactkeeper.viewModel.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, viewModel: ContactViewModel, contactId: String) {

    val contact by viewModel.selectedContact.collectAsState()

    LaunchedEffect(contactId) {
        viewModel.fetchContactById(contactId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile Screen",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF45C5FF)),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(CONSTANTS.HOME_SCREEN)
                            }
                            .padding(8.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF2F7FA))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                contact?.let {
                    Image(
                        contentScale = ContentScale.FillBounds,
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(20.dp)
                            .aspectRatio(1.5f)
                    )
                    DetailCard(label = "Name", value = it.name)
                    DetailCard(label = "Email", value = it.email)
                    DetailCard(label = "Phone no.", value = it.phoneNumber.firstOrNull() ?: "No phone number")
                    DetailCard(label = "Blood Group", value = it.bloodGroup)
                    DetailCard(label = "Address", value = it.address)

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )

                    Button(
                        onClick = { viewModel.deleteContact(it.id) },
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Delete Account")
                    }
                } ?: run {
                    Text(text = "Loading...", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun DetailCard(label: String, value: String) {
    Card(modifier = Modifier.padding(8.dp), colors = CardDefaults.cardColors(Color(0xFF9BD9FF))) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(NavHostController(LocalContext.current), ContactViewModel(LocalContext.current), "contactId")
}