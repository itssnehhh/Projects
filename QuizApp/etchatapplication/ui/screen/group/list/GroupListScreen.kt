package com.example.etchatapplication.ui.screen.group.list

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.GROUP_CHAT_SCREEN
import com.example.etchatapplication.model.Group
import com.example.etchatapplication.ui.theme.CustomGreen
import com.example.etchatapplication.ui.theme.DarkestGreen

@Composable
fun GroupListScreen(innerNavController: NavHostController) {

    val groupViewModel = hiltViewModel<GroupListScreenViewModel>()
    val groupList by groupViewModel.groupList.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(R.string.groups_title),
                color = CustomGreen,
                fontWeight = FontWeight.W600,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
        }
        item {
            if (groupList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.no_groups_available),
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            }
        }
        items(groupList) { group ->
            GroupChatListCard(group, innerNavController, groupViewModel)
        }
    }
}

@Composable
fun GroupChatListCard(
    group: Group,
    innerNavController: NavHostController,
    groupViewModel: GroupListScreenViewModel
) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                groupViewModel.markMessagesAsRead(group.id)
                innerNavController.navigate("$GROUP_CHAT_SCREEN/${group.id}")
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
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                if (group.unreadCount > 0) {
                    Text(
                        text = "${group.unreadCount} unread messages",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkestGreen
                    )
                } else {
                    Text(
                        text = group.lastMessage,
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
fun GroupScreenPreview() {
    GroupListScreen(NavHostController(LocalContext.current))
}