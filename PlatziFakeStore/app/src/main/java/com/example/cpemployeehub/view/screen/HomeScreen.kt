package com.example.cpemployeehub.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cpemployeehub.R


@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val categoryList = context.resources.getStringArray(R.array.homeCategoryList)
    val categoryImageList =
        listOf(R.drawable.icon6, R.drawable.icon3, R.drawable.icon2, R.drawable.icon7)
    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xFFF1E7FF))) {
        item {
            TopDetail()
            AttendanceCard()
            TimingCard()
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            )
        }
        item {
            LazyVerticalGrid(
                columns =  GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp)
            ) {
                items(categoryList.size) { index ->
                    CategoryCard(category = categoryList[index], image = categoryImageList[index])
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: String, image: Int) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFE2D0FD)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "",
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TimingCard() {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFE2D0FD)),
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
                    text = "09:00 AM",
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Checked In", fontWeight = FontWeight.SemiBold)
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
                    text = "--:--",
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Checked Out", fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.watch), contentDescription = "")
                Text(
                    text = "09:00",
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Working Hours", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun AttendanceCard() {
    Text(
        text = "Attendance",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
    )
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFD0B5FC)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "09:00 AM",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "Monday, June 10",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 4.dp),
            textAlign = TextAlign.Center
        )
        Button(
            colors = ButtonDefaults.buttonColors(Color(0xFFE96363)),
            onClick = {

            },
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
                    text = "Punch In",
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
                text = "Location :- You are in Office reach",
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
            .background(Color(0xFF854EE6))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "Welcome,",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = "Lance Williams",
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
    HomeScreen()
}