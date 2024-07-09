package com.example.employeehubapplication.view.screen.addEmployee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.employeehubapplication.R
import com.example.employeehubapplication.ui.theme.LightViolet
import com.example.employeehubapplication.ui.theme.TextFieldColor
import com.example.employeehubapplication.ui.theme.Violet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAddScreen(
    navController: NavHostController,
    viewModel: AddEmployeeViewModel,
    employeeId: Int?,
) {
    val context = LocalContext.current

    val name by viewModel.name.collectAsState()
    val jobTitle by viewModel.jobTitle.collectAsState()
    val email by viewModel.email.collectAsState()
    val contact by viewModel.contact.collectAsState()
    val address by viewModel.address.collectAsState()
    val dateOfBirth by viewModel.dateOfBirth.collectAsState()
    val bloodGroup by viewModel.bloodGroup.collectAsState()
    val expanded by viewModel.expanded.observeAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(Violet),
            title = {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.add_employee),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(LightViolet)
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .border(1.dp, Color.DarkGray)
                )

                UserDetailTextField(
                    value = name,
                    valueChange = { viewModel.onNameChange(it) },
                    label = stringResource(R.string.name),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                UserDetailTextField(
                    value = email,
                    valueChange = { viewModel.onEmailChange(it) },
                    label = stringResource(id = R.string.email_address),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email).copy(
                        imeAction = ImeAction.Next
                    )
                )

                UserDetailTextField(
                    value = contact,
                    valueChange = { viewModel.onContactChange(it) },
                    label = stringResource(id = R.string.phone_no),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number).copy(
                        imeAction = ImeAction.Next
                    )
                )

                UserDetailTextField(
                    value = jobTitle,
                    valueChange = { viewModel.onJobTitleChange(it) },
                    label = stringResource(R.string.job_title),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                UserDetailTextField(
                    value = address,
                    valueChange = { viewModel.onAddressChange(it) },
                    label = stringResource(R.string.address),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { viewModel.datePickerDialog.show() }
                ) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TextFieldColor,
                            unfocusedContainerColor = TextFieldColor,
                            disabledContainerColor = TextFieldColor,
                        ),
                        value = dateOfBirth,
                        onValueChange = { viewModel.onDateBirthChange(it) },
                        label = { Text(text = stringResource(R.string.date_of_birth)) },
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                Icons.Filled.DateRange,
                                contentDescription = "",
                                tint = Color.DarkGray
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
                            .background(TextFieldColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                                .clickable { viewModel.onExpandedChanges(true) },
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
                            expanded = expanded == true,
                            onDismissRequest = { viewModel.onExpandedChanges(false) }
                        ) {
                            viewModel.bloodGroups.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        viewModel.onBloodGroupChange(type)
                                        viewModel.onExpandedChanges(false)
                                    }
                                )
                            }
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.validation(
                            name,
                            email,
                            contact,
                            jobTitle,
                            address,
                            dateOfBirth,
                            employeeId,
                            context, navController
                        )

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (employeeId != 0) {
                            stringResource(R.string.btn_update)
                        } else {
                            stringResource(R.string.btn_save)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserDetailTextField(
    value: String,
    valueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = valueChange,
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TextFieldColor,
            unfocusedContainerColor = TextFieldColor,
            disabledContainerColor = TextFieldColor,
        ),
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        trailingIcon = trailingIcon,
        singleLine = true,
    )
}

