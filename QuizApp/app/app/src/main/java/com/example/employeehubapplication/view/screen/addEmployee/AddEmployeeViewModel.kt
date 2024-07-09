package com.example.employeehubapplication.view.screen.addEmployee

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.employeehubapplication.R
import com.example.employeehubapplication.data.local.EmployeeRepository
import com.example.employeehubapplication.data.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEmployeeViewModel(context: Context, private val repository: EmployeeRepository,employeeId: Int?) :
    ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _jobTitle = MutableStateFlow("")
    val jobTitle: StateFlow<String> = _jobTitle

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _contact = MutableStateFlow("")
    val contact: StateFlow<String> = _contact

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _dob = MutableStateFlow("")
    val dateOfBirth: StateFlow<String> = _dob

    val bloodGroups: Array<String> by mutableStateOf(
        context.resources.getStringArray(R.array.bloodGroupArray)
    )
    private val _bloodGroup = MutableStateFlow(bloodGroups[0])
    val bloodGroup: StateFlow<String> = _bloodGroup

    private val _expanded: MutableLiveData<Boolean> = MutableLiveData(false)
    val expanded: LiveData<Boolean> = _expanded

    // Validation error messages
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onJobTitleChange(job: String) {
        _jobTitle.value = job
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onContactChange(contact: String) {
        _contact.value = contact
    }

    fun onAddressChange(address: String) {
        _address.value = address
    }

    fun onExpandedChanges(status: Boolean) {
        _expanded.value = status
    }

    fun onDateBirthChange(dob: String) {
        _dob.value = dob
    }

    fun onBloodGroupChange(blood: String) {
        _bloodGroup.value = blood
    }

    private val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateBirthChange("$year-${month + 1}-$dayOfMonth")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    private fun insert(employee: Employee) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.insert(employee)
        }
    }

    private fun update(employee: Employee) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.update(employee)
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                getEmployeeById(employeeId)
            }
        }
    }

    private suspend fun getEmployeeById(employeeId: Int?){
       val employee = employeeId?.let { repository.getEmployeeById(it) }
        if (employee != null && employeeId != 0) {
            setValues(employee)
        }
    }

    private fun setValues(employee: Employee){
        _name.value = employee.name
        _jobTitle.value = employee.jobTitle
        _contact.value = employee.contactNo
        _email.value = employee.emailAddress
        _address.value = employee.address
        _bloodGroup.value = employee.bloodGroup
        _dob.value = employee.dateOfBirth
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    private fun isDateOfBirthValid(dob: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return try {
            val date = sdf.parse(dob)
            date != null && date.before(Date())
        } catch (e: Exception) {
            false
        }
    }


    fun validation(
        name: String,
        email: String,
        contact: String,
        jobTitle: String,
        address: String,
        dateOfBirth: String,
        employeeId: Int?,
        context: Context,
        navController: NavHostController,
    ) {
        if (name.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty() && jobTitle.isNotEmpty() && address.isNotEmpty() && dateOfBirth.isNotEmpty()) {
            val newEmp = Employee(
                id = employeeId ?: 0,
                name = name,
                jobTitle = jobTitle,
                contactNo = contact,
                emailAddress = email,
                address = address,
                bloodGroup = bloodGroup.value,
                dateOfBirth = dateOfBirth
            )
            if (employeeId != 0) {
                Log.d("TAG-UPDATE", "EmployeeAddScreen:$employeeId ")
                update(newEmp)
                navController.popBackStack()
            } else {
                Log.d("TAG-INSERT", "EmployeeAddScreen:$employeeId ")
                insert(newEmp)
                navController.popBackStack()
            }
            Toast.makeText(
                context,
                context.getString(R.string.detail_saved_successfully),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.please_fill_all_details),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}