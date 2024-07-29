package com.example.etchatapplication.ui.screen.group.chat

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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.GROUP_DETAIL_SCREEN
import com.example.etchatapplication.model.Message
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(innerNavController: NavHostController, groupId: String) {
    val groupChatViewModel = hiltViewModel<GroupChatViewModel>()
    val messages by groupChatViewModel.messages.collectAsState()
    val textMessage by groupChatViewModel.newMessage.collectAsState()
    val imageUri by groupChatViewModel.imageUri.collectAsState()

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
                            text = groupId,
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
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2BCA8D)),
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
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )
                    Image(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .clickable {
                                groupChatViewModel.sendMessage(groupId)
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
                    GroupChatMessageCard(message)
                }
            }
        }
    }
}

@Composable
fun GroupChatMessageCard(message: Message) {
    val isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.email
    val backgroundColor = if (isCurrentUser) Color(0xFF2BCA8D) else Color.Gray

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
                    text = message.senderId,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.Start)
                )
                if (message.imageUrl?.isNotEmpty() == true) {
                    println(message.imageUrl)
                    Image(
                        painter = rememberAsyncImagePainter(message.imageUrl),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(8.dp)
                    )
                }
                if (message.message.isNotEmpty()) {
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

