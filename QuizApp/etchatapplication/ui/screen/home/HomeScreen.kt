package com.example.etchatapplication.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.CHAT_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.USERS_LIST_SCREEN
import com.example.etchatapplication.model.ChatRoom
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val chatRoomList by homeViewModel.chatRoomList.collectAsState()

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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Text(
                    text = "Chats",
                    color = Color(0xFF2BCA8D),
                    fontWeight = FontWeight.W600,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            }
            item {
                if (chatRoomList.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No Chats available", modifier = Modifier.padding(16.dp))
                    }
                }
            }

            items(chatRoomList) { user ->
                UserChatList(user = user, navController)
            }
        }
    }
}

@Composable
fun UserChatList(user: ChatRoom, navController: NavHostController) {

    val currentUser = FirebaseAuth.getInstance().currentUser?.email ?: return
    val otherUser = user.participants.contains(currentUser)

    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("$CHAT_SCREEN/${user.participants}")
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            Image(
                painter = painterResource(id = R.drawable.account),
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(
                    text = user.chatroomId,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = user.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(NavHostController(LocalContext.current))
}