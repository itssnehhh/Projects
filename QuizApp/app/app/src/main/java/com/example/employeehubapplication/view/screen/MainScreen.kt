package com.example.employeehubapplication.view.screen

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.employeehubapplication.R
import com.example.employeehubapplication.data.local.EmployeeDatabase
import com.example.employeehubapplication.data.local.EmployeeRepository
import com.example.employeehubapplication.data.model.BottomNavItem
import com.example.employeehubapplication.view.screen.home.HomeScreen
import com.example.employeehubapplication.view.screen.Constant.ADD_EMPLOYEE_SCREEN
import com.example.employeehubapplication.view.screen.Constant.EMPLOYEE_LIST_SCREEN
import com.example.employeehubapplication.view.screen.Constant.HOME_SCREEN
import com.example.employeehubapplication.view.screen.Constant.NOTIFICATION_SCREEN
import com.example.employeehubapplication.view.screen.Constant.PROFILE_SCREEN
import com.example.employeehubapplication.view.screen.addEmployee.EmployeeAddScreen
import com.example.employeehubapplication.view.screen.addEmployee.AddEmployeeViewModel
import com.example.employeehubapplication.view.screen.employeeList.EmployeeListScreen
import com.example.employeehubapplication.view.screen.employeeList.EmployeeListViewModel
import com.example.employeehubapplication.view.screen.home.HomeViewModel
import com.example.employeehubapplication.view.screen.notification.NotificationScreen
import com.example.employeehubapplication.view.screen.notification.NotificationViewModel
import com.example.employeehubapplication.view.screen.profile.ProfileScreen
import com.example.employeehubapplication.view.screen.profile.ProfileViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Surface {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                if (currentRoute != ADD_EMPLOYEE_SCREEN && currentRoute?.startsWith("$ADD_EMPLOYEE_SCREEN/") == false) {
                    BottomNavigationBar(navController)
                }
            },
            content = { paddingValues ->
                NavHostContainer(navController = navController, paddingValues)
            }
        )
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

    val context = LocalContext.current
    val repository = EmployeeRepository(EmployeeDatabase.getInstance(context).employeeDao())

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(HOME_SCREEN) {
            val homeViewModel = HomeViewModel(context)
            HomeScreen(homeViewModel)
        }
        composable(EMPLOYEE_LIST_SCREEN) {
            val employeeViewModel = EmployeeListViewModel(repository)
            EmployeeListScreen(navController, employeeViewModel)
        }
        composable(NOTIFICATION_SCREEN) {
            val notificationViewModel = NotificationViewModel(context)
            NotificationScreen(notificationViewModel)
        }
        composable(PROFILE_SCREEN) {
            val profileViewModel = ProfileViewModel(context)
            ProfileScreen(profileViewModel)
        }
        composable("$ADD_EMPLOYEE_SCREEN/{employeeId}") { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId")?.toInt()
            val addEmployeeViewModel = AddEmployeeViewModel(context, repository, employeeId)
            employeeId?.let {
                EmployeeAddScreen(navController, addEmployeeViewModel, it)
            }
        }
    }
}

object Constant {
    const val HOME_SCREEN = "homeScreen"
    const val EMPLOYEE_LIST_SCREEN = "userListScreen"
    const val ADD_EMPLOYEE_SCREEN = "addUserScreen"
    const val NOTIFICATION_SCREEN = "notificationScreen"
    const val PROFILE_SCREEN = "profileScreen"
}
