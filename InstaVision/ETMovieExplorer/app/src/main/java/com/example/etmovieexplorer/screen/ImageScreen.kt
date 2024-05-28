package com.example.etmovieexplorer.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.utils.DownloadUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ImageScreen(imageUrl: String) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var downloadJob: Job? by remember { mutableStateOf(null) }
    var isImageLoading by remember { mutableStateOf(true) }
    var downloadStatus by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7E5FA))
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
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (isImageLoading) {
                CircularProgressIndicator()
            }
        }
        Text(text = imageUrl, textAlign = TextAlign.Center)

        Button(
            onClick = {
                downloadJob?.cancel()
                downloadJob = scope.launch {
                    DownloadUtils.downloadImage(
                        context = context,
                        imageUrl = imageUrl,
                        onComplete = { success ->
                            downloadStatus =
                                if (success) "Download Successful" else "Download Failed"
                            Toast.makeText(context, downloadStatus, Toast.LENGTH_SHORT).show()
                        },
                    )
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.btn_download))
        }
    }
}