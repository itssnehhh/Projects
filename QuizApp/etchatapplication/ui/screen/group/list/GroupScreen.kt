package com.example.etchatapplication.ui.screen.group.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.model.Group

@Composable
fun GroupScreen(innerNavController: NavHostController) {

    val groupViewModel = hiltViewModel<GroupViewModel>()
    val groups by groupViewModel.groups.collectAsState()
    Log.d("GROUPS", "GroupScreen: $groups")

    LazyColumn(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Groups",
                color = Color.Black,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(groups) { group ->
            GroupCard(group = group) { selectedGroup ->
                // Navigate to GroupChatScreen with group ID
                innerNavController.navigate("group_chat_screen/${selectedGroup.id}")
            }
        }
    }
}


@Composable
fun GroupCard(group: Group, onClick: (Group) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {
                onClick(group)
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Text(
                text = group.members.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
    HorizontalDivider()
}