package com.example.chatapplication.ui.screen.group.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.chatapplication.R
import com.example.chatapplication.constants.CONSTANTS.GROUP_DETAIL_SCREEN
import com.example.chatapplication.model.Message
import com.example.chatapplication.ui.theme.CustomGreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(innerNavController: NavHostController, groupId: String) {
    val groupChatViewModel = hiltViewModel<GroupChatViewModel>()
    val messages by groupChatViewModel.messages.collectAsState()
    val textMessage by groupChatViewModel.newMessage.collectAsState()
    val groupName by groupChatViewModel.groupName.collectAsState()

    LaunchedEffect(Unit) {
        groupChatViewModel.init(groupId)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { groupChatViewModel.uploadImageAndSendMessage(it) }
            groupChatViewModel.onImageUriChange(uri)
        }

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
                                .border(1.dp, Color.LightGray, CircleShape)
                        )
                        Text(
                            text = groupName,
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
                colors = TopAppBarDefaults.topAppBarColors(CustomGreen),
                modifier = Modifier.clickable {
                    innerNavController.navigate("$GROUP_DETAIL_SCREEN/$groupId")
                }
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.LightGray) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = textMessage,
                        onValueChange = { groupChatViewModel.updateMessage(it) },
                        maxLines = 4,
                        placeholder = { Text(text = "Type your message...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(painter = painterResource(id = R.drawable.media), contentDescription = null)
                    }
                    IconButton(onClick = { groupChatViewModel.sendMessage(groupId) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
                    }
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(messages) { message ->
                    GroupChatCard(viewModel = groupChatViewModel, message = message)
                }
            }
        }
    )
}

@Composable
fun GroupChatCard(viewModel: GroupChatViewModel, message: Message) {
    val senderName = viewModel.getSenderName(message.senderId)
    val isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid
    val backgroundColor = if (isCurrentUser) CustomGreen else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
    ) {
        Card(
            colors = CardDefaults.cardColors(backgroundColor),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(MaterialTheme.shapes.medium)
        ) {
            Column {
                Text(
                    text = senderName,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.Start)
                )
                if (message.imageUrl?.isNotBlank() == true) {
                    val painter = rememberAsyncImagePainter(message.imageUrl)
                    Box {
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(120.dp).padding(4.dp)
                        )
                    }
                }
                if (message.message.isNotBlank()) {
                    Text(
                        text = message.message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupChatScreenPreview() {
    GroupChatScreen(NavHostController(LocalContext.current), "1")
}