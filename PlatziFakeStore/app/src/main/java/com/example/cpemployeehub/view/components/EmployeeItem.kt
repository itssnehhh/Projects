package com.example.cpemployeehub.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.view.screen.EmployeeCard
import com.example.cpemployeehub.viewModel.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeItem(
    employee: Employee,
    onCardClick: () -> Unit,
    expanded: Boolean,
    navController: NavHostController,
    viewModel: EmployeeViewModel,
) {
    var show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(newValue = employee)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                show = false
                true
            } else false
        }, positionalThreshold = { 150.dp.toPx() }
    )
    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackGround(dismissState,currentItem,navController,viewModel)
            },
            dismissContent = {
                EmployeeCard(
                    card = employee,
                    onCardClick = onCardClick,
                    expanded = expanded
                )
            }
        )
    }
}
