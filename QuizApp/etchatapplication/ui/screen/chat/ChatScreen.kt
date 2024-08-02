package com.example.etchatapplication.ui.screen.chat

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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.IMAGE_SCREEN
import com.example.etchatapplication.model.Message
import com.example.etchatapplication.ui.theme.CustomGreen
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(innerNavController: NavHostController, userId: String?) {

    val chatViewModel = hiltViewModel<ChatViewModel>()
    val textMessage by chatViewModel.textMessage.collectAsState()
    val messages by chatViewModel.messages.collectAsState()
    val userDetails by chatViewModel.userDetails.collectAsState()
    val showPreview by chatViewModel.preview.collectAsState()

    LaunchedEffect(userId) {
        if (userId != null) {
            chatViewModel.getOrCreateRoom(userId)
            chatViewModel.loadUserDetails(userId)
        }
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                chatViewModel.uploadImageAndSendMessage(uri, textMessage)
            }
            chatViewModel.onImageUrlChange(uri)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = userDetails?.image, placeholder = painterResource(
                                    id = R.drawable.account
                                )
                            ),
                            contentDescription = "",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        )
                        Text(
                            text = "${userDetails?.firstname} ${userDetails?.lastname}",
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
                colors = TopAppBarDefaults.topAppBarColors(CustomGreen)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.LightGray) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = textMessage,
                        onValueChange = { chatViewModel.onTextMessageChange(it) },
                        maxLines = 4,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.enter_a_message),
                                color = Color.DarkGray
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
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
                                chatViewModel.sendMessage(textMessage)
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
                    ChatCard(message = message, innerNavController)
                }
                item {
                    if (showPreview) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.media),
                                contentDescription = "",
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatCard(message: Message, innerNavController: NavHostController) {

    val isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid
    val backgroundColor = if (isCurrentUser) CustomGreen else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
    ) {
        if (message.message.isNotBlank())
            Card(
                colors = CardDefaults.cardColors(backgroundColor),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                Text(
                    text = message.message,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        if (message.imageUrl?.isNotBlank() == true) {
            val painter = rememberAsyncImagePainter(
                model = message.imageUrl,
                placeholder = painterResource(id = R.drawable.account)
            )
            Box(
                modifier = Modifier.clickable {
                    val image =
                        URLEncoder.encode(message.imageUrl, StandardCharsets.UTF_8.toString())
                    innerNavController.navigate("$IMAGE_SCREEN/${image}")
                }
            ) {
                if (painter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = CustomGreen,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
    }
}
