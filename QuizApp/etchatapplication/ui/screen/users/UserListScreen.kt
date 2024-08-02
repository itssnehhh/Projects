package com.example.etchatapplication.ui.screen.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.etchatapplication.R
import com.example.etchatapplication.constants.CONSTANTS.CHAT_SCREEN
import com.example.etchatapplication.constants.CONSTANTS.GROUP_ADD_SCREEN
import com.example.etchatapplication.model.User
import com.example.etchatapplication.ui.common.LoadingDialog
import com.example.etchatapplication.ui.theme.CustomGreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavHostController) {

    val userListViewModel = hiltViewModel<UserListViewModel>()
    val userList by userListViewModel.userList.collectAsState()
    val isLoading by userListViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                title = {
                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(
                            text = stringResource(R.string.contacts),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (userList.size <= 1) stringResource(R.string.empty_contacts) else stringResource(
                                R.string.contact_list, userList.size - 1
                            ),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { userListViewModel.getUserList() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(CustomGreen)
            )
        }
    ) { paddingValues ->
        if (userList.size <= 1) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.no_users_available), color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                item {
                    GroupCard(navController)
                }
                items(userList) { user ->
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        if (user.email != currentUser.email) {
                            UserCard(user = user, navController = navController)
                        }
                    }
                }
            }
        }
        LoadingDialog(isLoading = isLoading)
    }
}

@Composable
fun UserCard(user: User, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable {
                navController.navigate("$CHAT_SCREEN/${user.id}")
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            Image(
                painter = if (user.image?.isNotEmpty() == true) {
                    rememberAsyncImagePainter(
                        model = user.image,
                        placeholder = painterResource(R.drawable.account)
                    )
                } else {
                    painterResource(id = R.drawable.account)
                },
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${user.firstname} ${user.lastname}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
    }
    HorizontalDivider()
}

@Composable
fun GroupCard(navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 4.dp)
            .clickable {
                navController.navigate(GROUP_ADD_SCREEN)
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "",
                modifier = Modifier
                    .padding(12.dp)
                    .size(40.dp)
            )
            Text(
                text = stringResource(R.string.new_group),
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )
        }
        HorizontalDivider()
    }
}