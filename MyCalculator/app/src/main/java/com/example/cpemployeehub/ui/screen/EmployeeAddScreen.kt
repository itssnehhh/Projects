package com.example.cpemployeehub.ui.screen

import android.app.DatePickerDialog
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cpemployeehub.R
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.viewModel.EmpViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(viewModel: EmpViewModel,employeeId: Int? = null, onEmployeeAdded: () -> Unit) {

    val context = LocalContext.current
    val employee = employeeId?.let { viewModel.getEmployeeById(it).observeAsState() }?.value
    var name by remember { mutableStateOf(employee?.name ?: "") }
    var email by remember { mutableStateOf(employee?.emailAddress ?: "") }
    var contactInfo by remember { mutableStateOf(employee?.contactNo ?: "") }
    var jobTitle by remember { mutableStateOf(employee?.jobTitle ?: "") }
    var address by remember { mutableStateOf(employee?.address ?: "") }
    var dateOfBirth by remember { mutableStateOf(employee?.dateOfBirth ?: "") }
    val bloodGroups = remember {
        context.resources.getStringArray(R.array.bloodGroupArray)
    }
    var bloodGroup by remember { mutableStateOf(employee?.bloodGroup ?: bloodGroups[0]) }
    val empImage by rememberSaveable { mutableStateOf("") }

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
                    label = { Text(text = "Name") },
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
                    label = { Text(text = "Email Address") },
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
                    label = { Text(text = "Contact No.") },
                    value = contactInfo,
                    onValueChange = { contactInfo = it },
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
                    label = { Text(text = "Job Title") },
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
                    label = { Text(text = "Address") },
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
                        if (name.isNotEmpty() && email.isNotEmpty() && contactInfo.isNotEmpty() && jobTitle.isNotEmpty() && address.isNotEmpty() && dateOfBirth.isNotEmpty()) {
                            val employee = Employee(
                                name = name,
                                jobTitle = jobTitle,
                                emailAddress = email,
                                contactNo = contactInfo,
                                address = address,
                                dateOfBirth = dateOfBirth,
                                bloodGroup = bloodGroup,
                                empImage = empImage
                            )
                            viewModel.insert(employee)
                            Toast.makeText(
                                context,
                                "$employee Detail Saved Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            onEmployeeAdded()
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
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

//@Preview
//@Composable
//fun AddEmployeeScreenPreview() {
//    AddEmployeeScreen(
//        NavHostController(LocalContext.current), EmployeeRepository(
//            EmployeeDao(
//                LocalContext.current
//            )
//        )
//    )
//}