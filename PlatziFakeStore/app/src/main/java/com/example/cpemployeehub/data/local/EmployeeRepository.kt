package com.example.cpemployeehub.data.local

import androidx.room.Query
import com.example.cpemployeehub.data.model.Employee
import kotlinx.coroutines.flow.Flow


class EmployeeRepository(private val employeeDao: EmployeeDao) {

    val getAllEmployee: Flow<List<Employee>> = employeeDao.getEmployeeList()

    suspend fun insert(employee: Employee) {
        employeeDao.insert(employee)
    }

    suspend fun update(employee: Employee) {
        employeeDao.update(employee)
    }

    suspend fun delete(employee: Employee) {
        employeeDao.delete(employee)
    }

    suspend fun getEmployeeById(employeeId: Int): Employee? {
        return employeeDao.getEmployeeById(employeeId)
    }
}