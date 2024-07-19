package com.example.etchatapplication.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etchatapplication.CONSTANTS.USERS_LIST_SCREEN
import com.example.etchatapplication.R

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(USERS_LIST_SCREEN) },
                containerColor = Color(0xFF2BCA8D)
            ) {
                Image(painter = painterResource(id = R.drawable.message), contentDescription = "")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                UserChatList()
            }
        }
    }
}

@Composable
fun UserChatList() {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {

            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.account),
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp)
            )
            Column(modifier = Modifier
                .padding(8.dp)
                .weight(1f)) {
                Text(
                    text = "Username",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = "lastMessage", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "00:00",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
    HorizontalDivider()
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(NavHostController(LocalContext.current))
}