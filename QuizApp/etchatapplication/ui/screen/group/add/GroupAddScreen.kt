package com.example.etchatapplication.ui.screen.group.add

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.etchatapplication.R
import com.example.etchatapplication.model.User
import com.example.etchatapplication.ui.screen.signup.InputTextField
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupAddScreen(innerNavController: NavHostController) {

    val groupAddViewModel = hiltViewModel<GroupAddViewModel>()
    val groupName by groupAddViewModel.groupName.collectAsState()
    val userList by groupAddViewModel.userList.collectAsState()
    val context = LocalContext.current

    var selectedUsers by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_a_group)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
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
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp)
                )
                InputTextField(
                    value = groupName,
                    onValueChange = { groupAddViewModel.onGroupNameChange(it) },
                    label = { Text(text = stringResource(R.string.enter_group_name)) }
                )
                Button(
                    onClick = {
                        if (groupName.isNotBlank() && selectedUsers.isNotEmpty()) {
                            groupAddViewModel.createGroup(groupName, selectedUsers) { groupId ->
                                Toast.makeText(
                                    context, "Group Created: $groupId",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            innerNavController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Please enter a group name and select users",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF2BCA8D)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(R.string.btn_create))
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
                HorizontalDivider()
            }
            items(userList) { user ->
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    if (user.email != currentUser.email) {
                        GroupUserCard(
                            user = user,
                            groupAddViewModel,
                            groupName,
                            selectedUsers
                        ) { isChecked ->
                            selectedUsers = if (isChecked) {
                                selectedUsers + user.email
                            } else {
                                selectedUsers - user.email
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupUserCard(
    user: User,
    groupAddViewModel: GroupAddViewModel,
    groupName: String,
    selectedUsers: List<String>,
    onUserSelect: (Boolean) -> Unit,
) {

    var isChecked by remember { mutableStateOf(selectedUsers.contains(user.email)) }

    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = TextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onUserSelect(it)
                },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2BCA8D))
            )
            Image(
                painter = painterResource(id = R.drawable.account),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp)
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