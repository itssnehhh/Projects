package com.example.cpemployeehub.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpemployeehub.data.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class EmployeeViewModel(application: Application) : AndroidViewModel(application) {


    private val _employeeState = MutableStateFlow(emptyList<Employee>())
    val employeeState: StateFlow<List<Employee>> = _employeeState.asStateFlow()

    private val _expandedCardList = MutableStateFlow(listOf<Int>())
    val expandedCardList: StateFlow<List<Int>> get() = _expandedCardList

    init {
        _employeeState.update { sampleEmployee() }
    }


    fun removeMessage(currentEmployee: Employee) {
        _employeeState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentEmployee)
            mutableList
        }
    }

    fun onCardClick(cardId: Int) {
        _expandedCardList.value = _expandedCardList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }


    private fun getFakeData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val employeeCardList = sampleEmployee()
                _employeeState.emit(employeeCardList)
            }
        }
    }

    private fun sampleEmployee() = listOf(
        Employee(
            1,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            2,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            3,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            4,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            5,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            6,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            7,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        ),
        Employee(
            8,
            "Stephen White",
            "Sr. Android Developer",
            "+1 98464484464654",
            emailAddress = "stephenWhite@gmail.com",
            address = "London",
            "A+",
            "05/06/1994",
            ""
        )
    )
}