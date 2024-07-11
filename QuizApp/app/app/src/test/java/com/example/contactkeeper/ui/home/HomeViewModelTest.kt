package com.example.contactkeeper.ui.home

import com.example.contactkeeper.data.model.Contact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.UUID

val contact =
    Contact(
        UUID.randomUUID().toString(),
        "User",
        listOf("9856473210", "9874563210"),
        "user@gmail.com",
        "",
        "A+",
        "Unknown"
    )


@ExperimentalCoroutinesApi
class HomeViewModelTest {


}