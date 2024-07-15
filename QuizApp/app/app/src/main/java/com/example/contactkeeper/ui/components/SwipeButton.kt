package com.example.contactkeeper.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.contactkeeper.ui.theme.CustomBlue
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeButton(
    text: String,
    isComplete: Boolean,
    modifier: Modifier = Modifier,
    doneImageVector: ImageVector = Icons.Rounded.Done,
    backgroundColor: Color = CustomBlue,
    onSwipe: () -> Boolean,
) {
    val width = 200.dp
    val widthInPx = with(LocalDensity.current) {
        width.toPx()
    }
    val anchors = mapOf(0F to 0, widthInPx to 1)
    val swipeableState = rememberSwipeableState(0)
    val (swipeComplete, setSwipeComplete) = remember {
        mutableStateOf(false)
    }
    val alpha: Float by animateFloatAsState(
        targetValue = if (swipeComplete) {
            0F
        } else {
            1F
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ), label = ""
    )

    LaunchedEffect(
        key1 = swipeableState.currentValue,
    ) {
        if (swipeableState.currentValue == 1) {
            if (onSwipe()) {
                setSwipeComplete(true)
            } else {
                swipeableState.snapTo(0)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                horizontal = 48.dp,
                vertical = 24.dp,
            )
            .clip(CircleShape)
            .background(backgroundColor)
            .animateContentSize()
            .then(
                if (swipeComplete) {
                    Modifier.width(64.dp)
                } else {
                    Modifier.fillMaxWidth()
                }
            )
            .requiredHeight(64.dp),
    ) {
        SwipeIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .alpha(alpha)
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ ->
                        FractionalThreshold(0.3F)
                    },
                    orientation = Orientation.Horizontal,
                ),
            backgroundColor = backgroundColor,
        )
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha)
                .padding(horizontal = 80.dp)
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
        )
        AnimatedVisibility(visible = swipeComplete && !isComplete) {
            CircularProgressIndicator(
                indicatorColor = Color.White,
                strokeWidth = 1.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.Center)
            )
        }
        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                imageVector = doneImageVector,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp),
            )
        }
    }
}

@Composable
fun SwipeIndicator(modifier: Modifier, backgroundColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .border(1.dp, Color.Gray, CircleShape)
            .clip(CircleShape)
            .aspectRatio(
                ratio = 1.0F,
                matchHeightConstraintsFirst = true,
            )
            .background(Color.White),
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = backgroundColor,
            modifier = Modifier.size(36.dp),
        )
    }
}