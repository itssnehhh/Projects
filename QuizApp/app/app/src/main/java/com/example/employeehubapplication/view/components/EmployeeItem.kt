package com.example.employeehubapplication.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
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
import androidx.navigation.NavHostController
import com.example.employeehubapplication.data.model.Employee
import com.example.employeehubapplication.view.screen.employeeList.EmployeeListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeItem(
    employee: Employee,
    onCardClick: () -> Unit,
    expanded: Boolean,
    navController: NavHostController,
    viewModel: EmployeeListViewModel,
) {
    val show by remember { mutableStateOf(true) }
    val currentItem by rememberUpdatedState(newValue = employee)
    val dismissState = rememberDismissState(
        confirmValueChange = { false }, positionalThreshold = { width -> width * 0.5f }
    )
    AnimatedVisibility(show, exit = fadeOut(spring())) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackGround(dismissState, currentItem, navController, viewModel)
            },
            dismissContent = {
                ExpandedEmployeeCard(
                    card = employee,
                    onCardClick = onCardClick,
                    expanded = expanded
                )
            }
        )
    }
}
