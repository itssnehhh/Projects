package com.example.cpemployeehub.ui.screen

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.cpemployeehub.R
import com.example.cpemployeehub.constatnt.Constant.ADD_EMPLOYEE_SCREEN
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.ui.components.EmailItem
import com.example.cpemployeehub.viewModel.EmpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(navController: NavHostController, viewModel: EmpViewModel) {

//    val employee by employeeViewModel.employeeState.collectAsState()
//    val expendedCardIds by employeeViewModel.expandedCardList.collectAsStateWithLifecycle(
//        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
//    )
    val employeeList by viewModel.allEmployees.observeAsState(emptyList())
    val expendedCardIds by viewModel.expandedCardList.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    Scaffold(topBar = {
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
            })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { navController.navigate(ADD_EMPLOYEE_SCREEN) },
            modifier = Modifier.padding(16.dp),
            containerColor = Color(0xFF6F41C0)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF1E7FF))
        ) {
            items(items = employeeList) { employee ->
                EmailItem(
                    employee = employee,
                    onRemove = { viewModel.deleteEmployee(it) },
                    onCardClick = { viewModel.onCardClick(employee.id) },
                    expanded = expendedCardIds.contains(employee.id),
                    navController = navController,
                    viewModel
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
        if (expanded) Color(0xFFE4D9F5) else Color(0xFFBE9BF8)
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
    val context = LocalContext.current
    val contentColour = remember {
        Color(ContextCompat.getColor(context, R.color.purple_500))
    }

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
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(20.dp))
            Text(
                text = emailAddress,
                textAlign = TextAlign.Center
            )
            Text(
                text = contactNo,
                textAlign = TextAlign.Center
            )
            Text(
                text = bloodGroup,
                textAlign = TextAlign.Center
            )
            Text(
                text = dateOfBirth,
                textAlign = TextAlign.Center
            )
            Text(
                text = address,
                textAlign = TextAlign.Center
            )
        }
    }
}
