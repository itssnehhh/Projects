package com.example.cpcontactkeeper.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Done
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.rememberAsyncImagePainter
import com.example.cpcontactkeeper.R
import com.example.cpcontactkeeper.data.model.Contact
import com.example.cpcontactkeeper.viewModel.ContactViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactAddScreen(contactViewModel: ContactViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val name by contactViewModel.name.collectAsState()
    val email by contactViewModel.email.collectAsState()
    val phoneNo by contactViewModel.phoneNo.collectAsState(emptyList())
    val address by contactViewModel.address.collectAsState()
    val imageUrl by contactViewModel.imageUrl.collectAsState()
    val expanded by contactViewModel.expanded.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    val (isComplete, setIsComplete) = remember {
        mutableStateOf(false)
    }
    val imageUri = remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri.value = uri
        uri?.let { contactViewModel.onImageUrlChange(it.toString()) }
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
                        painter = if (imageUri.value == null) {
                            painterResource(id = R.drawable.add_photo)
                        } else {
                            rememberAsyncImagePainter(model = imageUri.value)
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
                    valueChange = { contactViewModel.onNameChange(it) },
                    label = "Name"
                )

                phoneNo.forEachIndexed { index, phoneNo ->
                    DetailTextFields(
                        value = phoneNo,
                        valueChange = { contactViewModel.onPhoneNumberChange(index, it) },
                        label = "Phone no.",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Button(
                    onClick = { contactViewModel.addPhoneNumberField() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Add Phone Number")
                }
                DetailTextFields(
                    value = email,
                    valueChange = { contactViewModel.onEmailChange(it) },
                    label = "Email"
                )

                DetailTextFields(
                    value = address,
                    valueChange = { contactViewModel.onAddressChange(it) },
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
                                .clickable { contactViewModel.onExpandedChange(true) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = contactViewModel.bloodGroup,
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
                            onDismissRequest = { contactViewModel.onExpandedChange(false) }
                        ) {
                            contactViewModel.bloodGroups.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        contactViewModel.bloodGroup = type
                                        contactViewModel.onExpandedChange(false)
                                    }
                                )
                            }
                        }
                    }
                }
                SwipeButton(
                    text = "SAVE",
                    isComplete = isComplete,
                    onSwipe = {
                        coroutineScope.launch {
                            delay(2000)
                            setIsComplete(true)
                        }
                        val newContact = Contact(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            email = email,
                            phoneNumber = phoneNo,
                            profileImage = imageUri.value?.toString() ?: "",
                            bloodGroup = contactViewModel.bloodGroup,
                            address = address
                        )
                        contactViewModel.addContact(newContact)
                        Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()
                    },
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

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeButton(
    text: String,
    isComplete: Boolean,
    modifier: Modifier = Modifier,
    doneImageVector: ImageVector = Icons.Rounded.Done,
    backgroundColor: Color = Color(0xFF03A9F4),
    onSwipe: () -> Unit,
) {
    val width = 200.dp
    val widthInPx = with(LocalDensity.current) {
        width.toPx()
    }
    val anchors = mapOf(
        0F to 0,
        widthInPx to 1,
    )
    val swipeableState = rememberSwipeableState(0)
    val (swipeComplete, setSwipeComplete) = remember {
        mutableStateOf(false)
    }
    val alpha: Float by animateFloatAsState(
        targetValue = if (swipeComplete) {
            0F
        } else {
            1F
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ), label = ""
    )

    LaunchedEffect(
        key1 = swipeableState.currentValue,
    ) {
        if (swipeableState.currentValue == 1) {
            setSwipeComplete(true)
            onSwipe()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                horizontal = 48.dp,
                vertical = 24.dp,
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .animateContentSize()
            .then(
                if (swipeComplete) {
                    Modifier.width(64.dp)
                } else {
                    Modifier.fillMaxWidth()
                }
            )
            .requiredHeight(64.dp),
    ) {
        SwipeIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .alpha(alpha)
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.3F)
                    },
                    orientation = Orientation.Horizontal,
                ),
            backgroundColor = backgroundColor,
        )
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .padding(
                    horizontal = 80.dp,
                )
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                },
        )
        AnimatedVisibility(
            visible = swipeComplete && !isComplete,
        ) {
            CircularProgressIndicator(
                indicatorColor = Color.White,
                strokeWidth = 1.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
            )
        }
        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                imageVector = doneImageVector,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp),
            )
        }
    }
}

@Composable
fun SwipeIndicator(modifier: Modifier, backgroundColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .clip(CircleShape)
            .aspectRatio(
                ratio = 1.0F,
                matchHeightConstraintsFirst = true,
            )
            .background(Color.White),
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = backgroundColor,
            modifier = Modifier.size(36.dp),
        )
    }
}

@Preview
@Composable
fun ContactAddScreenPreview() {
    ContactAddScreen(
        ContactViewModel(LocalContext.current),
        NavHostController(LocalContext.current)
    )
}