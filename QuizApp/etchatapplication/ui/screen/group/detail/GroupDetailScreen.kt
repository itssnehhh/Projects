package com.example.etchatapplication.ui.screen.group.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.CONSTANTS.HOME_SCREEN
import com.example.etchatapplication.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    innerNavController: NavHostController,
    groupId: String,
) {
    val viewModel = hiltViewModel<GroupDetailViewModel>()
    val group by viewModel.group.collectAsState()
    val context = LocalContext.current
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadGroupDetails(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "") },
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = group?.name ?: "",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Divider()

                group?.users?.forEach { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.account),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(8.dp)
                            )
                            Text(
                                text = user.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        viewModel.exitGroup(groupId, currentUserEmail) { success ->
                            if (success) {
                                // Navigate back or show a message
                                innerNavController.navigate(HOME_SCREEN)
                            } else {
                                // Show an error message
                                Toast.makeText(context, "Failed to exit group", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red),
                ) {
                    Icon(painter = painterResource(id = R.drawable.logout), contentDescription = "")
                    Text(text = "Exit Group", color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupDetailScreenPreview() {
    GroupDetailScreen(innerNavController = NavHostController(LocalContext.current), groupId = "1")
}
