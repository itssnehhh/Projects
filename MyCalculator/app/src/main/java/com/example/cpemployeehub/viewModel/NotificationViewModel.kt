package com.example.cpemployeehub.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cpemployeehub.R
import com.example.cpemployeehub.data.model.Notification

class NotificationViewModel(context: Context) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    init {
        val notificationTypeList = context.resources.getStringArray(R.array.notificationTypeList)
        val notificationTitleList = context.resources.getStringArray(R.array.notificationTitleList)
        val notificationList = context.resources.getStringArray(R.array.notificationList)

        val notification = notificationList.indices.map { index ->
            Notification(
                title = notificationTitleList[index],
                type = notificationTypeList[index],
                notification = notificationList[index]
            )
        }
        _notifications.value = notification
    }
}