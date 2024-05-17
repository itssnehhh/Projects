package com.example.cpimagedownloadapplication.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.cpimagedownloadapplication.R
import com.example.cpimagedownloadapplication.utils.DownloadUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ImageScreen(imageUrl: String) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var downloadStatus by remember { mutableStateOf("") }
    var downloadProgress by remember { mutableIntStateOf(0) }
    var downloadJob: Job? by remember { mutableStateOf(null) }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7E5FA))
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .padding(2.dp)
        )
        Text(text = imageUrl, textAlign = TextAlign.Center)



        Button(
            onClick = {
                downloadJob = scope.launch {
                    DownloadUtils.downloadImage(
                        context = context,
                        imageUrl = imageUrl,
                        onProgressUpdate = { progress -> downloadProgress = progress },
                        onComplete = { success -> downloadStatus = if (success) "Download Successful" else "Download Failed" },
                        onCancel = { downloadStatus = "Download Cancelled" }
                    )
                }
            },    modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.btn_download))
        }
        Button(
            onClick = {
                downloadJob?.cancel()
                downloadStatus = "Download Cancelled"
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.btn_cancel))
        }
        if (downloadProgress > 0) {
            LinearProgressIndicator(progress = downloadProgress / 100f, modifier = Modifier.fillMaxWidth().padding(8.dp))
            Text(text = "Progress: $downloadProgress%", textAlign = TextAlign.Center)
        }

        Text(text = downloadStatus, textAlign = TextAlign.Center)
    }

}

@Preview
@Composable
fun ImageScreenPreview() {
    ImageScreen(imageUrl = "")
}