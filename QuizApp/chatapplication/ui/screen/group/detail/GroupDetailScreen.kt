package com.example.chatapplication.ui.screen.group.detail

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.chatapplication.R
import com.example.chatapplication.ui.theme.CustomGreen
import com.example.chatapplication.ui.theme.DarkestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    innerNavController: NavHostController,
    groupId: String
) {
    val viewModel = hiltViewModel<GroupDetailViewModel>()
    val group by viewModel.group.collectAsState()
    val isShowing by viewModel.show.collectAsState()
    val context = LocalContext.current
    val userDetails by viewModel.userDetails.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroupDetails(groupId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "", color = Color.White) },
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
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                // Display user details
                group?.users?.forEach { userId ->
                    val user = userDetails[userId]
                    Card(
                        colors = CardDefaults.cardColors(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .clip(TextFieldDefaults.shape)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = if (user?.image?.isNotEmpty() == true) {
                                    rememberAsyncImagePainter(model = user.image, placeholder = painterResource(
                                        id = R.drawable.account
                                    )) // Replace with user image resource
                                } else {
                                    painterResource(id = R.drawable.account)
                                },
                                contentDescription = "",
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(8.dp)
                            )
                            Text(
                                text = "${user?.firstname} ${user?.lastname}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (userId == group?.createdBy) {
                                Text(
                                    text = stringResource(R.string.admin),
                                    color = DarkestGreen,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        viewModel.showExitDialog(true)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red),
                ) {
                    Text(
                        text = stringResource(R.string.btn_exit),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    }

    if (isShowing) {
        AlertDialog(
            containerColor = CustomGreen,
            onDismissRequest = { viewModel.showExitDialog(true) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteGroup(groupId, context, innerNavController)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.exit),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showExitDialog(false) }) {
                    Text(
                        text = stringResource(R.string.btn_cancel),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.yes),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.exit_group_msg),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        )
    }
}


