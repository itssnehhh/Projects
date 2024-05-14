package com.example.cpmathquestquizapp.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cpmathquestquizapp.R
import com.example.cpmathquestquizapp.model.QuizCategory

val categoryImageList = intArrayOf(
    R.drawable.math,
    R.drawable.science,
    R.drawable.language,
    R.drawable.drama,
    R.drawable.art_craft,
    R.drawable.knowledge
)

@Composable
fun HomeScreen(navController: NavHostController) {

    val categoryTitleList = stringArrayResource(R.array.categoryList)
    val categoryTopicList = stringArrayResource(R.array.quizTopicList)
    val totalQuizzesList = stringArrayResource(R.array.totalQuizzes)

    val categoryItemList = remember {
        val categoryList = mutableListOf<QuizCategory>()
        for (i in categoryTitleList.indices) {
            categoryList.add(
                QuizCategory(
                    i + 1,
                    categoryImageList[i],
                    categoryTitleList[i],
                    categoryTopicList[i],
                    totalQuizzesList[i]
                )
            )
        }
        categoryList
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3FFF4))
    ) {
        item {
            TopBox(navController)
        }

        item {
            Spacer(modifier = Modifier.size(32.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.top_quiz_category),
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    TextButton(onClick = { }) {
                        Text(
                            text = stringResource(R.string.btn_see_all),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Blue
                        )
                    }
                }
            }
        }

        item {
            LazyRow {
                items(categoryItemList.size) {
                    CategoryCardImage(
                        image = categoryItemList[it].image, title = categoryItemList[it].title
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Live Quizzes",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        items(totalQuizzesList.size) {
            LiveQuizCard(
                image = categoryItemList[it].image,
                topic = categoryItemList[it].topic,
                subject = categoryItemList[it].title,
                totalQuiz = categoryItemList[it].totalQuiz
            )
        }
    }
}

@Composable
fun LiveQuizCard(image: Int, topic: String, subject: String, totalQuiz: String) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
    ) {
        Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = topic,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = subject,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(4.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .size(8.dp)
                    )
                    Text(
                        text = totalQuiz, fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Image(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "")
        }
    }
}

@Composable
fun CategoryCardImage(image: Int, title: String) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(100.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape),
        colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(42.dp)
            )
            Text(
                color = Color.Black,
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun TopBox(navController: NavHostController) {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                    color = Color(0xFFA4FC46)
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp),
                        text = "Good Morning 🌞",
                        color = Color.Black
                    )
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(4.dp),
                        text = stringResource(R.string.userName),
                        color = Color.Black
                    )
                }
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .size(64.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.user_image),
                    contentDescription = ""
                )
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (120).dp, x = (0).dp)
                        .border(1.dp, Color.DarkGray, CardDefaults.shape),
                    colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Start Your Quiz and Earn more Points",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(
                            border = BorderStroke(1.dp, Color.Gray),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F00C)),
                            onClick = {
                                Toast.makeText(context, "Quiz Will Start", Toast.LENGTH_SHORT).show()
                                navController.navigate("quiz_screen_route")
                            }, modifier = Modifier.padding(2.dp)
                        ) {
                            Text(text = "Start")
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ShowHomePreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}
