package com.example.cpemployeehub.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("employee_table")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("Id")
    val id: Int = 0,
    @ColumnInfo("Name")
    val name: String,
    @ColumnInfo("Job Title")
    val jobTitle: String,
    @ColumnInfo("Phone No")
    val contactNo: String,
    @ColumnInfo("Email Address")
    val emailAddress: String,
    @ColumnInfo("Address")
    val address: String,
    @ColumnInfo("Blood Group")
    val bloodGroup: String,
    @ColumnInfo("DOB")
    val dateOfBirth: String,
) : Parcelable