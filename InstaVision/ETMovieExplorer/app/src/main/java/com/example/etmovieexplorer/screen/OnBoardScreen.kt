package com.example.etmovieexplorer.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.etmovieexplorer.R
import com.example.etmovieexplorer.preferences.PrefManager
import com.example.etmovieexplorer.screen.Constants.LOGIN
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardScreen(navController: NavHostController) {
    val context = LocalContext.current

    val prefManager = remember { PrefManager(context) }
    val titles = remember { context.resources.getStringArray(R.array.onboard_title) }
    val desc = remember { context.resources.getStringArray(R.array.onboard_desc) }
    val pagerState = rememberPagerState(pageCount = { titles.size })

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                alpha = 0.9f,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                painter = painterResource(id = R.drawable.bg_image),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        TextButton(onClick = {
            prefManager.setOnBoardingStatus(true)
            navController.popBackStack()
            navController.navigate(LOGIN)
        }) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End,
                color = Color.Red,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.btn_skip),
            )
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom,
            ) { currentPage ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        color = Color.Red,
                        text = titles[currentPage],
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Text(
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        text = desc[currentPage],
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    )
                }
            }
            PagerIndicator(
                pageCount = titles.size,
                currentPage = pagerState.currentPage,
            )
            ButtonSelection(
                pagerState = pagerState,
                navController = navController,
                prefManager = prefManager
            )
        }
    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = 16.dp, label = "")
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(16.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.Red else Color.LightGray)
    )
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun previousPage(pagerState: PagerState) {
    val prevPage = pagerState.currentPage - 1
    if (prevPage >= 0) {
        pagerState.scrollToPage(prevPage)
    }
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun nextPage(pagerState: PagerState) {
    val nextPage = pagerState.currentPage + 1
    pagerState.scrollToPage(nextPage)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonSelection(
    pagerState: PagerState,
    navController: NavHostController,
    prefManager: PrefManager,
) {
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = pagerState.currentPage != 2,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = {
                    scope.launch { previousPage(pagerState) }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(text = stringResource(id = R.string.btn_previous))
            }
            Button(
                colors = ButtonDefaults.buttonColors(Color.Red),
                onClick = { scope.launch { nextPage(pagerState) } },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(text = stringResource(id = R.string.btn_next))
            }
        }
    }
    AnimatedVisibility(
        visible = pagerState.currentPage == 2,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        OutlinedButton(
            onClick = {
                prefManager.setOnBoardingStatus(true)
                navController.popBackStack()
                navController.navigate(LOGIN)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text(
                text = stringResource(R.string.btn_get_started),
            )
        }
    }
}

@Preview
@Composable
fun OnboardPreview() {
    OnBoardScreen(navController = NavHostController(LocalContext.current))
}