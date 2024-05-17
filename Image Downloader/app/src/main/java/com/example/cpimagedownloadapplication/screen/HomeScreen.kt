package com.example.cpimagedownloadapplication.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cpimagedownloadapplication.R
import com.example.cpimagedownloadapplication.constants.Constants.Companion.IMAGE_SCREEN
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF7E5FA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
            maxLines = 3,
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text(stringResource(R.string.et_message)) }
        )

        Button(
            onClick = {
                if (imageUrl.isNotEmpty()) {

                    val encodedUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate("$IMAGE_SCREEN/$encodedUrl")
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.unvalid_url_toast), Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(R.string.btn_show_image))
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}