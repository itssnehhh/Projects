package com.example.contactkeeper.ui.screen.addScreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.contactkeeper.R
import com.example.contactkeeper.data.model.Contact
import com.example.contactkeeper.ui.components.SwipeButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactAddScreen(
    navController: NavHostController,
    viewModel: ContactViewModel,
    contactID: String,
) {

    val context = LocalContext.current
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNo by viewModel.phoneNo.collectAsState(emptyList())
    val address by viewModel.address.collectAsState()
    val imageUrl by viewModel.imageUrl.collectAsState()
    val expanded by viewModel.expanded.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    val (isComplete, setIsComplete) = remember {
        mutableStateOf(false)
    }
    val contact by viewModel.selectedContact.collectAsState()

    println(contactID)

    LaunchedEffect(contactID) {
        if (contactID!="0")
        viewModel.fetchContactById(contactID)
    }
    contact?.let { viewModel.setValues(it) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadImageToFirebase(it,
                onSuccess = { url ->
                    viewModel.onImageUrlChange(url)
                    Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT)
                        .show()
                },
                onFailure = { exception ->
                    Toast.makeText(
                        context,
                        "Image upload failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF45C5FF)),
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                ) {
                    Image(
                        painter = if (imageUrl.isEmpty()) {
                            painterResource(id = R.drawable.add_photo)
                        } else {
                            rememberAsyncImagePainter(model = imageUrl)
                        },
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(8.dp)
                            .border(1.dp, Color.DarkGray, CircleShape)
                            .clip(CircleShape)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )

                DetailTextFields(
                    value = name,
                    valueChange = { viewModel.onNameChange(it) },
                    label = "Name"
                )
                DetailTextFields(
                    value = email,
                    valueChange = { viewModel.onEmailChange(it) },
                    label = "Email"
                )

                phoneNo.forEachIndexed { index, phoneNo ->
                    DetailTextFields(
                        value = phoneNo,
                        valueChange = { viewModel.onPhoneNumberChange(index, it) },
                        label = "Phone no.",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Button(
                    onClick = { viewModel.addPhoneNumberField() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Add Phone Number")
                }

                DetailTextFields(
                    value = address,
                    valueChange = { viewModel.onAddressChange(it) },
                    label = "Address"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.select_blood_group),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                                .clickable { viewModel.onExpandedChange(true) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = viewModel.bloodGroup,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            )
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { viewModel.onExpandedChange(false) }
                        ) {
                            viewModel.bloodGroups.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        viewModel.bloodGroup = type
                                        viewModel.onExpandedChange(false)
                                    }
                                )
                            }
                        }
                    }
                }

                SwipeButton(
                    text = if (contactID == "0") "SAVE" else "Update",
                    isComplete = isComplete,
                    onSwipe = {
                        coroutineScope.launch {
                            delay(2000)
                            setIsComplete(true)
                        }
                        val newContact = Contact(
                            id = if(contactID=="0") { UUID.randomUUID().toString() } else contactID,
                            name = name,
                            email = email,
                            phoneNumber = phoneNo,
                            profilePicture = imageUrl,
                            bloodGroup = viewModel.bloodGroup,
                            address = address
                        )
                        if (contactID=="0"){
                            viewModel.addContact(newContact)
                        }else{
                            viewModel.updateContact(newContact)
                        }
                        Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun DetailTextFields(
    value: String,
    valueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray
        ),
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        value = value,
        onValueChange = valueChange,
        label = { Text(text = label) }
    )
}

@Preview
@Composable
fun ContactAddScreenPreview() {
    ContactAddScreen(
        NavHostController(LocalContext.current),
        ContactViewModel(LocalContext.current),
        "it"
    )
}