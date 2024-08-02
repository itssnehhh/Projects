package com.example.etchatapplication.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.CHAT_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.USERS_LIST_SCREEN
import com.example.etchatapplication.model.ChatRoom
import com.example.etchatapplication.model.User
import com.example.etchatapplication.ui.theme.CustomGreen
import com.example.etchatapplication.ui.theme.DarkestGreen
import com.example.etchatapplication.ui.common.ChatRoomItem
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val chatRoomList by homeViewModel.chatRoomList.collectAsState()
    val userDetails by homeViewModel.userDetails.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(USERS_LIST_SCREEN) },
                containerColor = CustomGreen
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
                    text = stringResource(R.string.chats_title),
                    color = CustomGreen,
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
                        Text(
                            text = stringResource(R.string.no_chats_available),
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                }
            }
            items(chatRoomList) { chatRoom ->
                val otherUserId =
                    chatRoom.participants.first { it != FirebaseAuth.getInstance().currentUser?.uid }
                val user = userDetails[otherUserId]
                if (user != null) {
                    ChatRoomItem(
                        chatRoom = chatRoom,
                        navController = navController,
                        viewModel = homeViewModel,
                        user = user
                    )
                }
            }
        }
    }
}

@Composable
fun UserChatList(
    chatRoom: ChatRoom,
    user: User,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                homeViewModel.markMessagesAsRead(chatRoom.chatroomId) // Mark messages as read
                navController.navigate("$CHAT_SCREEN/${user.id}")
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.image,
                    placeholder = painterResource(id = R.drawable.account)
                ),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.DarkGray, CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "${user.firstname} ${user.lastname}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                if (chatRoom.unreadCount > 0) {
                    Text(
                        text = "${chatRoom.unreadCount} unread messages",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkestGreen
                    )
                } else {
                    Text(
                        text = chatRoom.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
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
