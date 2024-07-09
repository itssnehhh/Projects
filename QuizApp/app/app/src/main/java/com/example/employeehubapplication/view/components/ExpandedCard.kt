package com.example.employeehubapplication.view.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.employeehubapplication.data.model.Employee
import com.example.employeehubapplication.ui.theme.ExpandedPink
import com.example.employeehubapplication.ui.theme.ExpandedViolet
import com.example.employeehubapplication.view.components.Time.DURATION_TIME
import com.example.employeehubapplication.view.screen.employeeList.DefaultEmployeeCard
import com.example.employeehubapplication.view.screen.employeeList.ExpandableContent

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedEmployeeCard(card: Employee, onCardClick: () -> Unit, expanded: Boolean) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "")

    val cardBgColor by transition.animateColor({
        tween(durationMillis = DURATION_TIME)
    }, label = "") {
        if (expanded) ExpandedPink else ExpandedViolet
    }

    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = DURATION_TIME)
    }, label = "") {
        if (expanded) 12.dp else 8.dp
    }

    val cardElevation by transition.animateDp({
        tween(durationMillis = DURATION_TIME)
    }, label = "") {
        if (expanded) 24.dp else 4.dp
    }

    val contentColour = remember { Color.White }

    Card(
        onClick = onCardClick,
        colors = CardDefaults.cardColors(
            contentColor = contentColour,
            containerColor = cardBgColor
        ),
        elevation = CardDefaults.cardElevation(cardElevation),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                DefaultEmployeeCard(name = card.name, card.jobTitle)
            }
            ExpandableContent(
                visible = expanded,
                card.emailAddress,
                card.contactNo,
                card.bloodGroup,
                card.dateOfBirth,
                card.address
            )
        }
    }
}

object Time{
    const val DURATION_TIME = 200
}