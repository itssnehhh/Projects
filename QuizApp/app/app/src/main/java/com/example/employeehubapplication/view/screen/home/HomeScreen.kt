package com.example.employeehubapplication.view.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.employeehubapplication.R
import com.example.employeehubapplication.ui.theme.CardColor
import com.example.employeehubapplication.ui.theme.CustomRed
import com.example.employeehubapplication.ui.theme.CustomViolet
import com.example.employeehubapplication.ui.theme.LightViolet
import com.example.employeehubapplication.ui.theme.Violet

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    val categoryImageList by homeViewModel.categoryImageList.collectAsState()
    val categoryList by homeViewModel.categoryList.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightViolet)
    ) {
        item {
            TopDetail()
            AttendanceCard()
            TimingCard()
            Text(
                text = stringResource(R.string.category),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        item {
            LazyRow(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
            ) {
                itemsIndexed(categoryList) { index: Int, title: String ->
                    CategoryCard(category = title, categoryImageList[index])
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: String, image: Int) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(CardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            alignment = Alignment.Center,
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = category,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TimingCard() {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(CardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.checkedin), contentDescription = "")
                Text(
                    text = stringResource(id = R.string.time),
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(R.string.checked_in), fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.checkedout),
                    contentDescription = ""
                )
                Text(
                    text = stringResource(R.string.time_mark),
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(R.string.checked_out), fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.watch), contentDescription = "")
                Text(
                    text = stringResource(R.string.working_time),
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.working_hours),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun AttendanceCard() {
    Text(
        text = stringResource(R.string.attendance),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
    )
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(CustomViolet),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.time),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.date_day),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 4.dp),
            textAlign = TextAlign.Center
        )
        Button(
            colors = ButtonDefaults.buttonColors(CustomRed),
            onClick = {},
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.icon1),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                )
                Text(
                    text = stringResource(R.string.punch_in),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, end = 4.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "",
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.location),
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun TopDetail() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Violet)
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = stringResource(id = R.string.lance_williams),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeViewModel(LocalContext.current))
}