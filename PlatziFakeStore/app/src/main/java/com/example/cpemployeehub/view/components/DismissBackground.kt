package com.example.cpemployeehub.view.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cpemployeehub.constants.Constant.ADD_EMPLOYEE_SCREEN
import com.example.cpemployeehub.data.model.Employee
import com.example.cpemployeehub.viewModel.EmployeeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackGround(
    dismissState: DismissState,
    currentItem: Employee,
    navController: NavHostController,
    viewModel: EmployeeViewModel,
) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFF1DE9B6)
        DismissDirection.EndToStart -> Color(0xFFFF1744)
        null -> Color.Transparent
    }

    val context = LocalContext.current
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 8.dp)
            .background(color, CardDefaults.shape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if (direction == DismissDirection.StartToEnd) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "",
                modifier = Modifier.padding(start = 16.dp)
            )
            LaunchedEffect(Unit) {
                delay(500)
                navController.navigate("$ADD_EMPLOYEE_SCREEN/${currentItem.id}")
            }
        }
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier.padding(end = 16.dp)
            )
            LaunchedEffect(Unit) {
                delay(500)
                viewModel.deleteEmployee(currentItem)
                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}