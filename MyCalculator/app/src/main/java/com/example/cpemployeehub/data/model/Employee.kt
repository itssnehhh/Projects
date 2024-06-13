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
    val id: Int = 0,
    val name: String,
    val jobTitle: String,
    val contactNo: String,
    val emailAddress: String,
    val address: String,
    val bloodGroup: String,
    @ColumnInfo("DOB")
    val dateOfBirth: String,
    @ColumnInfo("Image")
    val empImage: String,
) : Parcelable
