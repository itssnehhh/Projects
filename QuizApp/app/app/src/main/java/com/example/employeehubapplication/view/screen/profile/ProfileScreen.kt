package com.example.employeehubapplication.view.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.employeehubapplication.R
import com.example.employeehubapplication.ui.theme.CardColor
import com.example.employeehubapplication.ui.theme.LightViolet
import com.example.employeehubapplication.ui.theme.Violet

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {

    val iconList by profileViewModel.iconList.collectAsState()
    val optionList by profileViewModel.optionList.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightViolet)
    ) {
        item {
            ProfileCard()
        }
        itemsIndexed(optionList) { index: Int, item: String ->
            OptionCard(option = item, item = iconList[index])
        }
    }
}

@Composable
fun OptionCard(option: String, item: Int) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(CardColor)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(32.dp)
            )
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = option, modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        colors = CardDefaults.cardColors(Violet),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            alignment = Alignment.TopCenter,
            painter = painterResource(id = R.drawable.user),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .size(160.dp)
        )
        Text(
            color = Color.White,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.lance_williams),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            color = Color.White,
            textAlign = TextAlign.Center,
            text = stringResource(R.string.android_developer),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(ProfileViewModel(LocalContext.current))
}