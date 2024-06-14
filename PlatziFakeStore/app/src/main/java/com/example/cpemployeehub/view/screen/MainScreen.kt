package com.example.cpemployeehub.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cpemployeehub.R
import com.example.cpemployeehub.constants.Constant.ADD_EMPLOYEE_SCREEN
import com.example.cpemployeehub.constants.Constant.EMPLOYEE_LIST_SCREEN
import com.example.cpemployeehub.constants.Constant.HOME_SCREEN
import com.example.cpemployeehub.constants.Constant.NOTIFICATION_SCREEN
import com.example.cpemployeehub.constants.Constant.PROFILE_SCREEN
import com.example.cpemployeehub.data.local.EmployeeDatabase
import com.example.cpemployeehub.data.local.EmployeeRepository
import com.example.cpemployeehub.data.model.BottomNavItem
import com.example.cpemployeehub.viewModel.EmpViewModelFactory
import com.example.cpemployeehub.viewModel.EmployeeViewModel
import com.example.cpemployeehub.viewModel.NotificationViewModel


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Surface {
        Scaffold(bottomBar = {
            BottomNavigationBar(navController)
        }, content = { paddingValues ->
            NavHostContainer(navController = navController, paddingValues)
        })
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = stringResource(id = R.string.home),
            icon = Icons.Default.Home,
            route = HOME_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.employee),
            icon = Icons.Filled.Add,
            route = EMPLOYEE_LIST_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.notification),
            icon = Icons.Default.Notifications,
            route = NOTIFICATION_SCREEN
        ),
        BottomNavItem(
            label = stringResource(id = R.string.profile),
            icon = Icons.Default.AccountCircle,
            route = PROFILE_SCREEN
        )
    )

    BottomAppBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        bottomNavItems.forEach { bottomNavItem ->
            NavigationBarItem(
                selected = currentRoute == bottomNavItem.route,
                onClick = {
                    navController.navigate(bottomNavItem.route)
                },
                icon = {
                    Icon(imageVector = bottomNavItem.icon, contentDescription = "")
                },
                label = {
                    Text(text = bottomNavItem.label)
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(HOME_SCREEN) {
            HomeScreen()
        }
        composable(EMPLOYEE_LIST_SCREEN) {
            val employeeViewModel: EmployeeViewModel = viewModel(
                factory = EmpViewModelFactory(
                    EmployeeRepository(
                        EmployeeDatabase.getInstance(
                            LocalContext.current
                        ).employeeDao()
                    )
                )
            )
            EmployeeListScreen(navController, employeeViewModel)
        }
        composable(NOTIFICATION_SCREEN) {
            val context = LocalContext.current
            val notificationViewModel = NotificationViewModel(context)
            NotificationScreen(notificationViewModel)
        }
        composable(PROFILE_SCREEN) {
            ProfileScreen()
        }
        composable("$ADD_EMPLOYEE_SCREEN/{employeeId}") { backStackEntry ->
            val employeeViewModel = EmployeeViewModel(
                EmployeeRepository(
                    EmployeeDatabase.getInstance(LocalContext.current).employeeDao()
                )
            )
            val employeeId = backStackEntry.arguments?.getString("employeeId")?.toInt()
            employeeId?.let {
                EmployeeAddScreen(navController, employeeViewModel,it)
            }

        }
    }
}
