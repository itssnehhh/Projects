package com.example.cpmathquestquizapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cpmathquestquizapp.R
import com.example.cpmathquestquizapp.model.Question

@Composable
fun QuizScreen(navController: NavHostController) {

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    val questions = generateQuestions()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (showResult) {
            ResultScreen(
                score = score,
                totalQuestions = questions.size,
                onRestart = {
                    currentQuestionIndex = 0
                    score = 0
                    showResult = false
                }
            )
        } else {
            QuizScreen(
                question = questions[currentQuestionIndex],
                onAnswerSelected = { selectedOptionIndex ->
                    val isCorrect =
                        selectedOptionIndex == questions[currentQuestionIndex].correctAnswer
                    if (isCorrect) score++
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                    } else {
                        showResult = true
                    }
                }
            )
        }
    }
}

@Composable
fun QuizScreen(
    question: Question,
    onAnswerSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.text,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        question.options.forEachIndexed { index, option ->

            RadioButton(
                selected = false,
                modifier = Modifier.padding(all = Dp(value = 8F)),
                onClick = {
                    onAnswerSelected(index)
                }
            )
            Text(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                text = option,
                modifier = Modifier.padding(start = 4.dp)
            )

        }
    }
}

@Composable
fun ResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Results",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Score: $score / $totalQuestions",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val excellenceLevel = when {
            score <= totalQuestions / 2 -> "Poor"
            score < totalQuestions -> "Good"
            else -> "Very Good"
        }
        Text(
            text = "Excellence Level: $excellenceLevel",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = onRestart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Restart Quiz")
        }
    }
}

@Composable
fun generateQuestions(): List<Question> {

    val questionList = stringArrayResource(id = R.array.questionList)
    val firstOptionList = stringArrayResource(id = R.array.firstOptionList)
    val secondOptionList = stringArrayResource(id = R.array.secondOptionList)
    val thirdOptionList = stringArrayResource(id = R.array.thirdOptionList)
    val fourthOptionList = stringArrayResource(id = R.array.fourthOptionList)

    val correctOption = stringArrayResource(id = R.array.answerList).map { it.toInt() }


    val questions = mutableListOf<Question>()
    for (i in questionList.indices) {
        questions.add(
            Question(
                questionList[i],
                listOf(
                    firstOptionList[i],
                    secondOptionList[i],
                    thirdOptionList[i],
                    fourthOptionList[i]
                ),
                correctOption[i]
            )
        )
    }
    return questions
}