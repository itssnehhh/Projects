package com.example.cpemployeehub.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cpemployeehub.R
import com.example.cpemployeehub.viewModel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel) {

    val notification by notificationViewModel.notifications.observeAsState(emptyList())

    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFF854EE6)),
            title = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.notification),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        LazyColumn(
            modifier = Modifier
                .background(Color(0xFFF1E7FF))
                .fillMaxSize()
        ) {
            items(notification) { notification ->
                NotificationCard(
                    type = notification.type,
                    title = notification.title,
                    notification = notification.notification
                )
            }
        }
    }
}

@Composable
fun NotificationCard(notification: String, type: String, title: String) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFE2D0FD)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(1.dp, Color.Black, CardDefaults.shape)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.TwoTone.Notifications,
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
            )
            Column {
                Text(
                    text = type,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Text(
                    text = "$title :- $notification",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        notificationViewModel = NotificationViewModel(LocalContext.current)
    )
}