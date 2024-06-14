package com.example.cpemployeehub.view.screen

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cpemployeehub.R
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.viewModel.EmployeeViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeAddScreen(
    navController: NavHostController,
    viewModel: EmployeeViewModel,
    employeeId: Int?,
) {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var jobTitle by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var contact by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var dateOfBirth by rememberSaveable { mutableStateOf("") }
    val bloodGroups = remember { context.resources.getStringArray(R.array.bloodGroupArray) }
    var bloodGroup by rememberSaveable { mutableStateOf(bloodGroups[0]) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = "$year-${month + 1}-$dayOfMonth"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val employee by viewModel.getEmployeeById(employeeId ?: 0).observeAsState()

    LaunchedEffect(employee) {
        employee?.let { emp ->
            name = emp.name
            jobTitle = emp.jobTitle
            email = emp.emailAddress
            contact = emp.contactNo
            address = emp.address
            dateOfBirth = emp.dateOfBirth
            bloodGroup = emp.bloodGroup
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(Color(0xFF854EE6)),
            title = {
                Text(
                    color = Color.White,
                    text = "ADD EMPLOYEE",
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
                .background(Color(0xFFF1E7FF))
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

                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0E4FA),
                        unfocusedContainerColor = Color(0xFFE0E4FA),
                        disabledContainerColor = Color(0xFFE0E4FA),
                    ),
                    label = { Text(text = stringResource(R.string.name)) },
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0E4FA),
                        unfocusedContainerColor = Color(0xFFE0E4FA),
                        disabledContainerColor = Color(0xFFE0E4FA),
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = { Text(text = stringResource(R.string.email_address)) },
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0E4FA),
                        unfocusedContainerColor = Color(0xFFE0E4FA),
                        disabledContainerColor = Color(0xFFE0E4FA),
                    ),
                    label = { Text(text = stringResource(R.string.phone_no)) },
                    value = contact,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { contact = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0E4FA),
                        unfocusedContainerColor = Color(0xFFE0E4FA),
                        disabledContainerColor = Color(0xFFE0E4FA),
                    ),
                    label = { Text(text = stringResource(R.string.job_title)) },
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE0E4FA),
                        unfocusedContainerColor = Color(0xFFE0E4FA),
                        disabledContainerColor = Color(0xFFE0E4FA),
                    ),
                    label = { Text(text = stringResource(R.string.address)) },
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .clickable { datePickerDialog.show() }
                ) {
                    OutlinedTextField(
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE0E4FA),
                            unfocusedContainerColor = Color(0xFFE0E4FA),
                            disabledContainerColor = Color(0xFFE0E4FA),
                        ),
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
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
                            .background(Color(0xFFCBD2FD))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black)
                                .padding(4.dp)
                                .clickable { expanded = true },
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
                            onDismissRequest = { expanded = false }
                        ) {
                            bloodGroups.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        bloodGroup = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        if (name.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty() && jobTitle.isNotEmpty() && address.isNotEmpty() && dateOfBirth.isNotEmpty()) {
                            val newEmp = Employee(
                                id = employeeId ?: 0,
                                name = name,
                                jobTitle = jobTitle,
                                contactNo = contact,
                                emailAddress = email,
                                address = address,
                                bloodGroup = bloodGroup,
                                dateOfBirth = dateOfBirth
                            )
                            if (employeeId != 0) {
                                Log.d("TAG-UPDATE", "EmployeeAddScreen:$employeeId ")
                                viewModel.update(newEmp)
                            } else {
                                Log.d("TAG-INSERT", "EmployeeAddScreen:$employeeId ")
                                viewModel.insert(newEmp)
                            }
                            Toast.makeText(
                                context,
                                context.getString(R.string.detail_saved_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}
