package com.example.cpemployeehub.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cpemployeehub.constatnt.Constant.ADD_EMPLOYEE_SCREEN
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.ui.screen.EmployeeCard
import com.example.cpemployeehub.viewModel.EmpViewModel
import com.example.employeehubapplication.ui.components.DismissBackGround
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailItem(
    employee: Employee,
    onRemove: (Employee) -> Unit,
    onCardClick: () -> Unit,
    expanded: Boolean,
    navController: NavHostController,
    viewModel: EmpViewModel,
) {
    val context = LocalContext.current
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
            background = { DismissBackGround(dismissState = dismissState) },
            dismissContent = {
                EmployeeCard(
                    card = employee,
                    onCardClick = onCardClick,
                    expanded = expanded
                )
            }
        )
    }

    LaunchedEffect(Unit) {
        if (!show) {
            delay(500)
            if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                viewModel.deleteEmployeeById(currentItem.id)
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
            } else if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                navController.navigate("$ADD_EMPLOYEE_SCREEN/${employee.id}")
            }
        }
    }
}