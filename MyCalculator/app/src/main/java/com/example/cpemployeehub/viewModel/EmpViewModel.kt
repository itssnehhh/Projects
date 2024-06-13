package com.example.cpemployeehub.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cpemployeehub.data.local.EmployeeRepository
import com.example.cpemployeehub.data.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EmpViewModel(private val repository: EmployeeRepository) : ViewModel() {

    val allEmployees: LiveData<List<Employee>> = repository.getAllEmployee.asLiveData()

    private val _expandedCardList = MutableStateFlow(listOf<Int>())
    val expandedCardList: StateFlow<List<Int>> get() = _expandedCardList

    fun insert(employee: Employee) = viewModelScope.launch {
        repository.insert(employee)
    }
    fun update(employee: Employee) = viewModelScope.launch {
        repository.update(employee)
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(employee)
        }
    }
    fun deleteEmployeeById(employeeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEmployeeById(employeeId)
        }
    }

    fun getEmployeeById(id: Int): LiveData<Employee> {
        return repository.getEmployeeById(id).asLiveData()
    }

    fun onCardClick(cardId: Int) {
        _expandedCardList.value = _expandedCardList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
        }
    }
}

class EmpViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}