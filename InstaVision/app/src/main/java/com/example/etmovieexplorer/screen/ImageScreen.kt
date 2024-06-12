package com.example.etmovieexplorer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.utils.DownloadUtils.downloadImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ImageScreen(imageUrl: String) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var downloadStatus by remember { mutableStateOf("") }
    var downloadJob: Job? by remember { mutableStateOf(null) }
    var isImageLoading by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(2.dp), contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(model = imageUrl)

            isImageLoading = when (painter.state) {
                is AsyncImagePainter.State.Loading -> true
                is AsyncImagePainter.State.Success -> false
                is AsyncImagePainter.State.Error -> false
                else -> false
            }
            Image(
                painter = painter,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (isImageLoading) {
                CircularProgressIndicator(color = Color.Red)
            }
        }
        Row {
            Button(
                modifier = Modifier.padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = {
                    downloadJob?.cancel()
                    downloadJob = scope.launch {
                        downloadImage(
                            context = context,
                            imageUrl = imageUrl,
                            onComplete = { success ->
                                downloadStatus =
                                    if (success)
                                        context.getString(R.string.image_download_successful)
                                    else
                                        context.getString(R.string.download_failed)
                            },
                        )
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.btn_download))
            }
        }
    }
}

@Preview
@Composable
fun ImageScreenPreview() {
    ImageScreen(imageUrl = "")
}