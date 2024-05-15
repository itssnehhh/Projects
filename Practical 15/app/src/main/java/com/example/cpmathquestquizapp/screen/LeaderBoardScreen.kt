package com.example.cpmathquestquizapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cpmathquestquizapp.R
import com.example.cpmathquestquizapp.model.Score

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderBoardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF00F00C)),
                title = {
                    Text(
                        text = stringResource(R.string.leaderboard_screen),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }) {

        val nameList = stringArrayResource(id = R.array.usersList)
        val scoreList = integerArrayResource(id = R.array.scoreList)

        val userScoreList = remember {

            val leaderboardList = mutableListOf<Score>()

            for (i in nameList.indices) {
                leaderboardList.add(Score(i + 1, nameList[i], scoreList[i]))
            }
            leaderboardList
        }

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    TimingCard()
                }
            }
            itemsIndexed(userScoreList) { index, _ ->
                UserScoreCard(
                    id = userScoreList[index].id,
                    name = userScoreList[index].name,
                    score = userScoreList[index].score
                )
            }
        }
    }
}

@Composable
fun UserScoreCard(id: Int, name: String, score: Int) {

    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    Color(0xFFA6F7AC)
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, Color.Black, CircleShape),

                ) {
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(8.dp),
                    text = "$id",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                color = MaterialTheme.colorScheme.secondary,
                text = name,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Image(painter = painterResource(id = R.drawable.coin), contentDescription = "")
            Text(
                color = MaterialTheme.colorScheme.onSecondary,
                text = "$score",
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun TimingCard() {

    val timingList = stringArrayResource(id = R.array.timingList)

    timingList.forEach {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.Gray, CardDefaults.shape),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun ShowLeaderBoardScreenPreview() {
    LeaderBoardScreen()
}