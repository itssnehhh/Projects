package com.example.cpmathquestquizapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cpmathquestquizapp.R

@Composable
fun ResultScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .border(1.dp, Color.LightGray, CardDefaults.shape),
            colors = CardDefaults.cardColors(Color(0xFFE2FFC2))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
//                    Text(text = "$correctAnswers", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 2.dp))
//                    Text(text = "/", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 2.dp))
//                    Text(text = "$totalQuestions", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 2.dp))
                }
                Button(
                    onClick = {
//                        onRestartClicked()
                    },
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .border(1.dp, Color.DarkGray, ButtonDefaults.shape),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00F00C))

                ) {
                    Text(text = stringResource(R.string.btn_restart), fontSize = 18.sp, modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}