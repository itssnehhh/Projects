package com.example.cpdrinkexplorer.screen

import MocktailViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpdrinkexplorer.R
import com.example.cpdrinkexplorer.data.model.Cocktail
import com.example.cpdrinkexplorer.screen.Constants.DETAIL_SCREEN
import com.example.cpdrinkexplorer.screen.Constants.FAVOURITE_SCREEN
import com.example.cpdrinkexplorer.screen.Constants.SEARCH_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: MocktailViewModel) {

    val drinkList by viewModel.mocktails.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.mocktails
    }

    Scaffold(topBar = {
        TopAppBar(
            actions = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { navController.navigate(SEARCH_SCREEN) })
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { navController.navigate(FAVOURITE_SCREEN) }
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFE5F322)
            )
        )
    }) { innerPadding ->

//        var searchText by remember { mutableStateOf("") }
//        var searchedDrinkList by remember { mutableStateOf<List<Cocktail>>(emptyList()) }
//        var errorMessage by remember { mutableStateOf("") }
//        var isLoading by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.banner),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(drinkList) { drink ->
                    DrinkCard(navController = navController, response = drink)
                }
            }
        }
    }
}

@Composable
fun DrinkCard(navController: NavHostController, response: Cocktail) {

    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .border(1.dp, Color.LightGray, CardDefaults.shape)
            .clickable {
                navController.navigate(DETAIL_SCREEN)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = response.strDrinkThumb),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = "",
                tint = Color(0xFFE5F322),
                modifier = Modifier
                    .offset(y = (20).dp)
                    .padding(8.dp)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                "Drink added to your favourite list",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .background(Color.Black, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
        Text(
            color = Color(0xFF77A002),
            maxLines = 2,
            text = response.strDrink,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .fillMaxSize(),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = 3,
            text = response.strInstructions
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = NavHostController(LocalContext.current),
        viewModel = MocktailViewModel()
    )
}