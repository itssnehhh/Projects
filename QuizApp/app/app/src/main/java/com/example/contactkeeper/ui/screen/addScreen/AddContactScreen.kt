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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.contactkeeper.R
import com.example.contactkeeper.ui.components.SwipeButton
import com.example.contactkeeper.ui.theme.CustomBlue
import com.example.contactkeeper.ui.theme.CustomGrey
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    navController: NavHostController,
    contactID: String,
) {

    val viewModel = hiltViewModel<AddContactViewModel>()
    val context = LocalContext.current
    val name by viewModel.name.collectAsState("")
    val email by viewModel.email.collectAsState("")
    val phoneNo by viewModel.phoneNo.collectAsState(emptyList())
    val address by viewModel.address.collectAsState("")
    val imageUrl by viewModel.imageUrl.collectAsState("")
    val expanded by viewModel.expanded.observeAsState(initial = false)
    val isComplete by remember { mutableStateOf(false) }
    val bloodGroupList by remember {
        mutableStateOf(context.resources.getStringArray(R.array.bloodGroupArray))
    }

    val bloodGroup by viewModel.bloodGroup.collectAsState()
    val contact by viewModel.selectedContact.collectAsState()
    if (contactID != "0") {
        viewModel.fetchContactById(contactID)
        contact?.let { viewModel.setValues(it) }
    } else {
        viewModel.apply {
            onNameChange("")
            onEmailChange("")
            onAddressChange("")
            onImageUrlChange("")
            onPhoneNumberChange(0, "")
            onBloodGroupChange(bloodGroupList[0])
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadImageToFirebase(it,
                onSuccess = { imageUrl ->
                    viewModel.onImageUrlChange(imageUrl)
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
                colors = TopAppBarDefaults.topAppBarColors(CustomBlue),
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(CustomGrey),
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
                    label = stringResource(R.string.name),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                DetailTextFields(
                    value = email,
                    valueChange = { viewModel.onEmailChange(it) },
                    label = stringResource(R.string.email),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(
                        imeAction = ImeAction.Next
                    )
                )
                val addIcon = Icons.Default.Add
                val deleteIcon = Icons.Default.Delete

                phoneNo.forEachIndexed { index, phoneNo ->
                    DetailTextFields(
                        value = phoneNo,
                        valueChange = { viewModel.onPhoneNumberChange(index, it) },
                        label = stringResource(R.string.phone_no),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(
                            imeAction = ImeAction.Next
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = if (index > 0) deleteIcon else addIcon,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(32.dp)
                                    .clickable {
                                        when {
                                            addIcon == Icons.Default.Add -> if (index > 0) {
                                                viewModel.onPhoneNumberChange(index, "")
                                                viewModel.removePhoneNumberField()
                                            } else {
                                                viewModel.addPhoneNumberField()
                                            }
                                        }
                                    }
                            )
                        }
                    )
                }
                DetailTextFields(
                    value = address,
                    valueChange = { viewModel.onAddressChange(it) },
                    label = stringResource(R.string.address),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
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
                                text = bloodGroup,
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
                            bloodGroupList.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        viewModel.onBloodGroupChange(type)
                                        viewModel.onExpandedChange(false)
                                    }
                                )
                            }
                        }
                    }
                }
                SwipeButton(
                    text = if (contactID == "0") stringResource(R.string.btn_save) else stringResource(
                        R.string.btn_update
                    ),
                    isComplete = isComplete,
                    onSwipe = {
                        viewModel.verifyDetails(
                            contactID = if (contactID == "0") UUID.randomUUID()
                                .toString() else contactID,
                            name = name,
                            email = email,
                            phoneNumber = phoneNo,
                            profilePicture = imageUrl,
                            bloodGroup = bloodGroup,
                            address = address,
                            context = context,
                            navController = navController
                        )
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
    trailingIcon: @Composable (() -> Unit)? = null
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
        label = { Text(text = label) },
        trailingIcon = trailingIcon
    )
}

@Preview
@Composable
fun ContactAddScreenPreview() {
    AddContactScreen(
        NavHostController(LocalContext.current),
        "it"
    )
}