package com.example.mathquestquizapplication.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mathquestquizapplication.R
import com.example.mathquestquizapplication.model.Questions

@Composable
fun QuizScreen(navController: NavHostController) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            }
            item {
                StartQuizScreen()
            }
        }
    }
}

@Composable
fun StartQuizScreen() {

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val questions = generateQuestions()

    if (showResult) {
        ResultCard(
            score = score,
            totalQuestions = questions.size,
            onRestart = {
                currentQuestionIndex = 0
                score = 0
                showResult = false
            }
        )
    } else {
        QuizCard(
            question = questions[currentQuestionIndex],
            onAnswerSelected = { selectedOptionIndex ->
                val isCorrect =
                    selectedOptionIndex == questions[currentQuestionIndex].correctAnswer

                if (isCorrect)
                    score++

                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    showResult = true
                }
            },
            currentQuestionIndex = currentQuestionIndex
        )
    }
}

@Composable
fun QuizCard(question: Questions, onAnswerSelected: (Int) -> Unit, currentQuestionIndex: Int) {

    var selectedOption by remember {
        mutableStateOf("")
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "$currentQuestionIndex",
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "/",
                fontSize = 28.sp,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = "15",
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }

        val animatedProgress by animateFloatAsState(
            targetValue = (currentQuestionIndex) / generateQuestions().size.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp),
            trackColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.16f),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(Color(0xFFE2FFC2)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .border(1.dp, Color.Black, CardDefaults.shape)
                .size(160.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold,
                    text = question.question,
                    modifier = Modifier
                        .padding(16.dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        question.options.forEachIndexed { index, option ->
            Card(
                colors = CardDefaults.cardColors(Color(0xFFE2FFC2)),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    text = option,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onSelectionChange(option) })
                        .border(
                            width = 2.dp,
                            Color.LightGray,
                            CardDefaults.shape
                        )
                        .selectable(
                            selected = (option.contentEquals(option)),
                            onClick = { onAnswerSelected(index) }
                        )
                        .padding(16.dp)
                )
            }
        }

        Button(
            onClick = {

            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F00C))
        ) {
            Text(
                text = stringResource(R.string.btn_next),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun ResultCard(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .border(1.dp, Color.LightGray, CardDefaults.shape),
        colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.trophy),
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                text = stringResource(R.string.congratulations),
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Text(
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                text = stringResource(R.string.tv_score), fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "$score",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "/",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                Text(
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = "$totalQuestions",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
            val excellenceLevel = when {
                score <= totalQuestions / 2 -> stringResource(R.string.poor)
                score < totalQuestions -> stringResource(R.string.good)
                else -> stringResource(R.string.very_good)
            }
            Text(
                color = MaterialTheme.colorScheme.onSecondary,
                text = stringResource(R.string.excellence_level, excellenceLevel),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Button(
                onClick = onRestart,
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .border(1.dp, Color.DarkGray, ButtonDefaults.shape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F00C))

            ) {
                Text(
                    text = stringResource(R.string.btn_restart),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun generateQuestions(): List<Questions> {

    val questionList = stringArrayResource(id = R.array.questionList)
    val firstOptionList = stringArrayResource(id = R.array.firstOptionList)
    val secondOptionList = stringArrayResource(id = R.array.secondOptionList)
    val thirdOptionList = stringArrayResource(id = R.array.thirdOptionList)
    val fourthOptionList = stringArrayResource(id = R.array.fourthOptionList)

    val correctOptionList = stringArrayResource(id = R.array.answerList).map { it.toInt() }

    val questions = mutableListOf<Questions>()

    for (i in questionList.indices) {
        questions.add(
            Questions(
                questionList[i],
                listOf(
                    firstOptionList[i],
                    secondOptionList[i],
                    thirdOptionList[i],
                    fourthOptionList[i]
                ),
                correctOptionList[i]
            )
        )
    }
    return questions
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreen(navController = NavHostController(LocalContext.current))
}
