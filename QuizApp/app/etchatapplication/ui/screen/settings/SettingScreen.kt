package com.example.etchatapplication.ui.screen.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(darkTheme: Boolean, darkThemeChange: () -> Unit) {
    Text(text = "Setting Screen")

}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(false) { }
}