package com.example.cpmathquestquizapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cpmathquestquizapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00F00C)),
                title = {
                    Text(
                        text = stringResource(R.string.profile_screen),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color(0xFFF3FFF4))
        ) {
            item {
                UserDetailCard()
            }
            item {
                Text(
                    text = stringResource(R.string.achievements_title),
                    modifier = Modifier.padding(12.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                LazyRow {
                    item {
                        UserAchievementCard()
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(2.dp)
                        .background(Color.Black)
                )
            }
            item {
                AppSettingsCards()
            }
        }
    }
}

@Composable
fun AppSettingsCards() {
    Column {
        SettingSwitchCards()
        Spacer(
            modifier = Modifier
                .padding(4.dp)
                .height(2.dp),
        )
        SettingsList()
    }
}

val settingsImageList = intArrayOf(R.drawable.share, R.drawable.help, R.drawable.logout)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsList() {

    val settingsList = stringArrayResource(id = R.array.settingsList)

    for (i in settingsList.indices) {
        OutlinedCard(
            onClick = { },
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Image(
                    painter = painterResource(id = settingsImageList[i]),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    fontWeight = FontWeight.Bold,
                    text = settingsList[i],
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun SettingSwitchCards() {

    var darkTheme by remember {
        mutableStateOf(false)
    }
    var notificationOnOff by remember {
        mutableStateOf(true)
    }

    val switchTitleList = stringArrayResource(id = R.array.switchTitleList)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, CardDefaults.shape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = switchTitleList[0],
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Switch(
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF488303),
                    uncheckedTrackColor = Color(0xFFEAF8DA)
                ),
                checked = darkTheme,
                onCheckedChange = { darkTheme = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                fontWeight = FontWeight.Bold,
                text = switchTitleList[1],
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Switch(
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF488303),
                    uncheckedTrackColor = Color(0xFFEAF8DA)
                ),
                checked = notificationOnOff,
                onCheckedChange = { notificationOnOff = it },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

val achievementImageList = intArrayOf(
    R.drawable.silver_medal,
    R.drawable.silver_medal,
    R.drawable.silver_medal,
    R.drawable.silver_medal,
    R.drawable.bronze_medal,
    R.drawable.bronze_medal,
    R.drawable.bronze_medal,
    R.drawable.bronze_medal
)

@Composable
fun UserAchievementCard() {

    val achievementList = stringArrayResource(id = R.array.achievementList)

    for (i in achievementList.indices) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
        ) {
            Image(
                painter = painterResource(id = achievementImageList[i]),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Text(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                text = achievementList[i],
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun UserDetailCard() {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.user_image),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.userId),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = Color.Gray
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically),
                        text = stringResource(R.string.userName),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically),
                        text = stringResource(R.string.mailId),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowProfilePreview() {
    ProfileScreen()
}