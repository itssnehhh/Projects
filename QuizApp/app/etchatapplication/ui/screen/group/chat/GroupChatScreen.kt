package com.example.etchatapplication.ui.screen.group.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(innerNavController: NavHostController, groupId: String) {

    val groupChatViewModel = hiltViewModel<GroupChatViewModel>()
    val messages by groupChatViewModel.messages.collectAsState()
    val newMessage by groupChatViewModel.newMessage.collectAsState()

    LaunchedEffect(key1 = groupId) {
        groupChatViewModel.groupId = groupId
        groupChatViewModel.fetchMessages()
    }

    Log.d("MESSAGES", "GroupChatScreen: ${groupChatViewModel.fetchMessages()}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.account),
                            contentDescription = "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(4.dp)
                                .border(1.dp, Color.DarkGray, CircleShape)
                        )
                        Text(
                            text = "Group: $groupId",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { innerNavController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2BCA8D))
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.LightGray) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = newMessage,
                        onValueChange = { groupChatViewModel.updateMessage(it) },
                        maxLines = 4,
                        placeholder = { Text(text = stringResource(R.string.enter_a_message)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .clip(CardDefaults.shape)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.media),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                    Image(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .clickable {
                                groupChatViewModel.sendMessage()
                            }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(messages) { message ->
                    Text(
                        text = message.message,
                        color = if (message.senderId == FirebaseAuth.getInstance().currentUser?.email) Color.Blue else Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupChatScreenPreview() {
    GroupChatScreen(NavHostController(LocalContext.current), "sampleGroupId")
}
