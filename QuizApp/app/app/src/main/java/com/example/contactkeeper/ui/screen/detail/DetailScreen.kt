package com.example.contactkeeper.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.contactkeeper.R
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.screen.CONSTANTS
import com.example.contactkeeper.ui.theme.CustomBlue
import com.example.contactkeeper.ui.theme.CustomGrey
import com.example.contactkeeper.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    contactId: String?,
) {

    val viewModel = hiltViewModel<DetailViewModel>()
    val contact by viewModel.selectedContact.collectAsState(initial = null)
    val showDialog by viewModel.showDialog.collectAsState()

    LaunchedEffect(contactId) {
        if (contactId != null) {
            viewModel.fetchContactById(contactId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_screen),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(CustomBlue),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { navController.navigate(CONSTANTS.HOME_SCREEN) }
                            .padding(8.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(CustomGrey)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                contact?.let {
                    Image(
                        contentScale = ContentScale.Inside,
                        painter = rememberAsyncImagePainter(model = it.profilePicture),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 12.dp)
                            .aspectRatio(1.5f)
                    )
                    DetailCard(label = stringResource(id = R.string.name), value = it.name)
                    DetailCard(label = stringResource(id = R.string.email), value = it.email)
                    DetailCard(
                        label = stringResource(id = R.string.phone_no),
                        value = it.phoneNumber.toString()
                    )
                    DetailCard(label = stringResource(R.string.blood_group), value = it.bloodGroup)
                    DetailCard(label = stringResource(id = R.string.address), value = it.address)

                    Button(
                        onClick = { viewModel.onDialogStatusChange(true) },
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = stringResource(R.string.delete_account))
                    }
                } ?: run {
                    Text(
                        text = stringResource(R.string.loading),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        if (showDialog) {
            DeleteDialog(viewModel = viewModel, contact = contact, navController = navController)
        }
    }
}

@Composable
fun DeleteDialog(
    viewModel: DetailViewModel,
    contact: Contact?,
    navController: NavHostController,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { viewModel.onDialogStatusChange(true) },
        confirmButton = {
            TextButton(
                onClick = {
                    contact?.let {
                        viewModel.deleteContact(it.id)
                        viewModel.deleteImageFromFirebase(it.profilePicture)
                    }
                    viewModel.onDialogStatusChange(false)
                    Toast.makeText(
                        context,
                        context.getString(R.string.item_removed),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.onDialogStatusChange(false)
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { Text(text = stringResource(R.string.delete_contact)) },
        text = { Text(text = stringResource(R.string.delete_detail_message)) }
    )
}

@Composable
fun DetailCard(label: String, value: String) {
    Card(modifier = Modifier.padding(8.dp), colors = CardDefaults.cardColors(LightBlue)) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        NavHostController(LocalContext.current),
        ""
    )
}