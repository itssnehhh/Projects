package com.example.universitydirectoryapplication.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.universitydirectoryapplication.R
import com.example.universitydirectoryapplication.ui.screen.Constant.SEARCH_SCREEN
import com.example.universitydirectoryapplication.ui.theme.CustomRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, homeViewModel: HomeViewModel) {

    val imageList by homeViewModel.imageList.collectAsState()
    val universityTitleList by homeViewModel.titleList.collectAsState()
    val universityCityList by homeViewModel.cityList.collectAsState()
    val categoryList by homeViewModel.categoryList.collectAsState()
    val catImageList by homeViewModel.catImageList.collectAsState()
    val articleList by homeViewModel.articleList.collectAsState()
    val articleImageList by homeViewModel.articleImageList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(CustomRed),
                title = {
                    Text(
                        text = stringResource(R.string.university_directory),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                },
                actions = {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { navController.navigate(SEARCH_SCREEN) }
                            .padding(8.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                Text(
                    text = stringResource(R.string.top_university),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp)
                )
            }
            itemsIndexed(universityTitleList) { index, titleList ->
                TopUniversities(imageList[index], titleList, universityCityList[index])
            }
            item {
                Text(
                    text = stringResource(R.string.category),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp)
                )
            }
            item {
                LazyRow {
                    itemsIndexed(categoryList) { index: Int, title: String ->
                        CategoryList(title, catImageList[index])
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.articles),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
            itemsIndexed(articleList) { index: Int, article: String ->
                ArticleCard(article, articleImageList[index])
            }
        }
    }
}

@Composable
fun ArticleCard(article: String, image: Int) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = article,
            maxLines = 3,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )
    }
}

@Composable
fun CategoryList(title: String, image: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(text = title, modifier = Modifier.padding(8.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun TopUniversities(image: Int, title: String, city: String) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(id = image),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.pin, city),
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
