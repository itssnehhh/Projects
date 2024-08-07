package com.example.mappractice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapScreen() {

    val state by remember { mutableStateOf(MapState()) }

    Scaffold { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            properties = state.properties,
            uiSettings = MapUiSettings(zoomControlsEnabled = true),
            onMapLongClick = {

            }
        )
    }
}

data class MapState(
    val properties: MapProperties = MapProperties(),
    val isFalloutMap: Boolean = false,
)