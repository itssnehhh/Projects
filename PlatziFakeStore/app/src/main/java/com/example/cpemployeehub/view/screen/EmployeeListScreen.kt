package com.example.cpemployeehub.view.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
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
import com.example.cpemployeehub.R
import com.example.cpemployeehub.constants.Constant.ADD_EMPLOYEE_SCREEN
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.view.components.EmployeeItem
import com.example.cpemployeehub.viewModel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(navController: NavHostController, viewModel: EmployeeViewModel) {

    val employeeList by viewModel.allEmployees.observeAsState(emptyList())
    val expendedCardIds by viewModel.expandedCardList.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF854EE6)),
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
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFF6F41C0)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
    ) { paddingValues ->
        if (employeeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF1E7FF))
            ) {
                items(items = employeeList) { employee ->
                    EmployeeItem(
                        employee = employee,
                        onCardClick = { viewModel.onCardClick(employee.id) },
                        expanded = expendedCardIds.contains(employee.id),
                        navController = navController,
                        viewModel
                    )
                }
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF1E7FF))
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "No employees onboarded yet.",
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

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeCard(card: Employee, onCardClick: () -> Unit, expanded: Boolean) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "transition")

    val cardBgColor by transition.animateColor({
        tween(durationMillis = 200)
    }, label = "bgColorTransition") {
        if (expanded) Color(0xFFEF7DFF) else Color(0xFFBE9BF8)
    }

    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = 200)
    }, label = "paddingTransition") {
        if (expanded) 12.dp else 8.dp
    }

    val cardElevation by transition.animateDp({
        tween(durationMillis = 200)
    }, label = "elevationTransition") {
        if (expanded) 24.dp else 4.dp
    }

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 200)
    }, label = "rotationDegreeTransition") {
        if (expanded) 180f else 0f
    }
    val contentColour = remember { Color.White }

    Card(
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            contentColor = contentColour,
            containerColor = cardBgColor
        ),
        elevation = CardDefaults.cardElevation(cardElevation),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(degrees = arrowRotationDegree)
                CardTitle(name = card.name, card.jobTitle)
            }
            ExpandableContent(
                visible = expanded,
                card.emailAddress,
                card.contactNo,
                card.bloodGroup,
                card.dateOfBirth,
                card.address
            )
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
) {
    IconButton(
        onClick = {},
        content = {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(name: String, jobTitle: String) {
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
            animationSpec = tween(200)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(200)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(200)
        ) + fadeOut(animationSpec = tween(200))
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
                text = "Email Address :- $emailAddress",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Mobile No. :- $contactNo",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Blood Group :- $bloodGroup",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall

            )
            Text(
                text = "Date Of Birth :- $dateOfBirth",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Address :- $address",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}