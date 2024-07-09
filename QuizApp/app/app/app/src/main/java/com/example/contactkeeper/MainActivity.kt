package com.example.contactkeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.contactkeeper.ui.screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}


//class ContactViewModel(context: Context) : ViewModel() {
//
//    // Existing code...
//
//    private var pendingImageUri: Uri? = null
//
//    fun setImageUri(uri: Uri) {
//        pendingImageUri = uri
//    }
//
//    fun clearImageUri() {
//        pendingImageUri = null
//    }
//
//    fun uploadPendingImageToFirebase(
//        onSuccess: (String) -> Unit,
//        onFailure: (Exception) -> Unit
//    ) {
//        pendingImageUri?.let { uri ->
//            uploadImageToFirebase(uri,
//                onSuccess = { url ->
//                    onSuccess(url)
//                    clearImageUri() // Clear the pending image URI after successful upload
//                },
//                onFailure = onFailure
//            )
//        }
//    }
//
//    // Existing code...
//
//}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ContactAddScreen(
//    navController: NavHostController,
//    viewModel: ContactViewModel,
//    contactID: String,
//) {
//
//    val context = LocalContext.current
//    val name by viewModel.name.collectAsState()
//    val email by viewModel.email.collectAsState()
//    val phoneNo by viewModel.phoneNo.collectAsState(emptyList())
//    val address by viewModel.address.collectAsState()
//    val imageUrl by viewModel.imageUrl.collectAsState()
//    val expanded by viewModel.expanded.observeAsState(initial = false)
//    val coroutineScope = rememberCoroutineScope()
//    val (isComplete, setIsComplete) = remember {
//        mutableStateOf(false)
//    }
//    val contact by viewModel.selectedContact.collectAsState()
//
//    println(contactID)
//
//    LaunchedEffect(contactID) {
//        if (contactID!="0")
//            viewModel.fetchContactById(contactID)
//    }
//    contact?.let { viewModel.setValues(it) }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            viewModel.setImageUri(it) // Set the image URI in ViewModel
//        }
//    }
//
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { },
//                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF45C5FF)),
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.Center
//        ) {
//            item {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .clickable {
//                            launcher.launch("image/*")
//                        }
//                ) {
//                    Image(
//                        painter = if (imageUrl.isEmpty()) {
//                            painterResource(id = R.drawable.add_photo)
//                        } else {
//                            rememberAsyncImagePainter(model = imageUrl)
//                        },
//                        contentScale = ContentScale.Crop,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(8.dp)
//                            .border(1.dp, Color.DarkGray, CircleShape)
//                            .clip(CircleShape)
//                    )
//                }
//                Spacer(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(20.dp)
//                )
//
//                DetailTextFields(
//                    value = name,
//                    valueChange = { viewModel.onNameChange(it) },
//                    label = "Name"
//                )
//                DetailTextFields(
//                    value = email,
//                    valueChange = { viewModel.onEmailChange(it) },
//                    label = "Email"
//                )
//
//                phoneNo.forEachIndexed { index, phoneNo ->
//                    DetailTextFields(
//                        value = phoneNo,
//                        valueChange = { viewModel.onPhoneNumberChange(index, it) },
//                        label = "Phone no.",
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                }
//
//                Button(
//                    onClick = { viewModel.addPhoneNumberField() },
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                ) {
//                    Text(text = "Add Phone Number")
//                }
//
//                DetailTextFields(
//                    value = address,
//                    valueChange = { viewModel.onAddressChange(it) },
//                    label = "Address"
//                )
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                ) {
//                    Text(
//                        fontWeight = FontWeight.Bold,
//                        text = stringResource(R.string.select_blood_group),
//                        style = MaterialTheme.typography.titleMedium,
//                    )
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .background(Color.LightGray)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .border(1.dp, Color.Black)
//                                .padding(4.dp)
//                                .clickable { viewModel.onExpandedChange(true) },
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                textAlign = TextAlign.Center,
//                                text = viewModel.bloodGroup,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(4.dp)
//                            )
//                            Icon(
//                                Icons.Filled.ArrowDropDown,
//                                contentDescription = "",
//                                modifier = Modifier.padding(horizontal = 8.dp)
//                            )
//                        }
//                        DropdownMenu(
//                            expanded = expanded,
//                            onDismissRequest = { viewModel.onExpandedChange(false) }
//                        ) {
//                            viewModel.bloodGroups.forEach { type ->
//                                DropdownMenuItem(
//                                    text = { Text(text = type, fontSize = 16.sp) },
//                                    onClick = {
//                                        viewModel.bloodGroup = type
//                                        viewModel.onExpandedChange(false)
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//
//                SwipeButton(
//                    text = if (contactID == "0") "SAVE" else "Update",
//                    isComplete = isComplete,
//                    onSwipe = {
//                        coroutineScope.launch {
//                            delay(2000)
//                            setIsComplete(true)
//                        }
//                        viewModel.uploadPendingImageToFirebase(
//                            onSuccess = { url ->
//                                viewModel.onImageUrlChange(url)
//                                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT)
//                                    .show()
//                            },
//                            onFailure = { exception ->
//                                Toast.makeText(
//                                    context,
//                                    "Image upload failed: ${exception.message}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        )
//                        val newContact = Contact(
//                            id = if(contactID=="0") { UUID.randomUUID().toString() } else contactID,
//                            name = name,
//                            email = email,
//                            phoneNumber = phoneNo,
//                            profilePicture = imageUrl, // Use the imageUrl directly from ViewModel
//                            bloodGroup = viewModel.bloodGroup,
//                            address = address
//                        )
//                        if (contactID=="0"){
//                            viewModel.addContact(newContact)
//                        }else{
//                            viewModel.updateContact(newContact)
//                        }
//                        Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT)
//                            .show()
//                        navController.popBackStack()
//                    }
//                )
//            }
//        }
//    }
//}

//@Composable
//fun DetailTextFields(
//    value: String,
//    valueChange: (String) -> Unit,
//    label: String,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//) {
//    TextField(
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = Color.LightGray,
//            unfocusedContainerColor = Color.LightGray
//        ),
//        keyboardOptions = keyboardOptions,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        value = value,
//        onValueChange = valueChange,
//        label = { Text(text = label) }
//    )
//}
//
//@Preview
//@Composable
//fun ContactAddScreenPreview() {
//    ContactAddScreen(
//        NavHostController(LocalContext.current),
//        ContactViewModel(LocalContext.current),
//        "it"
//    )
//}
