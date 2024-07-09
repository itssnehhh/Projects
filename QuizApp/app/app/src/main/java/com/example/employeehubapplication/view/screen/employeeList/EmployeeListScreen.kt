package com.example.employeehubapplication.view.screen.employeeList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.employeehubapplication.R
import com.example.employeehubapplication.ui.theme.LightViolet
import com.example.employeehubapplication.ui.theme.Violet
import com.example.employeehubapplication.view.components.EmployeeItem
import com.example.employeehubapplication.view.components.Time.DURATION_TIME
import com.example.employeehubapplication.view.screen.Constant.ADD_EMPLOYEE_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(navController: NavHostController, viewModel: EmployeeListViewModel) {

    val employeeList by viewModel.allEmployees.observeAsState(emptyList())
    val expendedCardIds by viewModel.expandedCardList.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Violet),
                title = {
                    Text(
                        color = Color.White,
                        text = stringResource(R.string.employee_list),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("$ADD_EMPLOYEE_SCREEN/${0}") },
                containerColor = Violet
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
    ) { paddingValues ->
        if (employeeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightViolet)
            ) {
                items(items = employeeList) { employee ->
                    EmployeeItem(
                        employee = employee,
                        onCardClick = { viewModel.onCardClick(employee.id) },
                        expanded = expendedCardIds.contains(employee.id),
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightViolet)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.no_employees_onboarded_yet),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DefaultEmployeeCard(name: String, jobTitle: String) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
            Text(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                text = jobTitle,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun ExpandableContent(
    visible: Boolean = true,
    emailAddress: String,
    contactNo: String,
    bloodGroup: String,
    dateOfBirth: String,
    address: String,
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(DURATION_TIME)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(DURATION_TIME)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(DURATION_TIME)
        ) + fadeOut(animationSpec = tween(DURATION_TIME))
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.text_email, emailAddress),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(R.string.mobile_no, contactNo),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.blood_group, bloodGroup),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall

            )
            Text(
                text = stringResource(R.string.dob, dateOfBirth),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = stringResource(R.string.textAddress, address),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

