package com.example.cpdrinkexplorer.screen

import MocktailViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpdrinkexplorer.R
import com.example.cpdrinkexplorer.data.model.Cocktail

@Composable
fun DetailScreen(
    mocktailId: String,
    viewModel: MocktailViewModel,
    navController: NavHostController,
) {
    // Observe selectedMocktail from ViewModel
    val selectedMocktail by viewModel.selectedMocktail.collectAsState()

    // Fetch the selected mocktail when DetailScreen is first composed
    LaunchedEffect(mocktailId) {
        viewModel.selectMocktail(mocktailId)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCCF8FAEA))
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE5F322))
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.DarkGray
                    )
                }
            }
            selectedMocktail?.let { DrinkDetail(it) }
        }
    }
}

@Composable
fun DrinkDetail(mocktail: Cocktail) {
    Image(
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.DarkGray)
            .height(250.dp),
        painter = rememberAsyncImagePainter(model = mocktail.strDrinkThumb ?: R.drawable.ic_launcher_background),
        contentDescription = null
    )
    Text(
        fontWeight = FontWeight.Bold,
        text = mocktail.strDrink,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.headlineSmall,
        color = Color(0xFF77A002)
    )
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = "Category: ${mocktail.category}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Type: ${mocktail.type}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Text(
        text = stringResource(R.string.instructions),
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        style = MaterialTheme.typography.bodyMedium,
        text = mocktail.strInstructions
    )
    Text(
        text = stringResource(R.string.ingredients),
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.titleLarge
    )
    val ingredients = listOfNotNull(
        mocktail.strIngredient1,
        mocktail.strIngredient2,
        mocktail.strIngredient3,
        mocktail.strIngredient4
    ).joinToString(", ")
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        style = MaterialTheme.typography.bodyMedium,
        text = ingredients
    )
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        navController = NavHostController(LocalContext.current),
        mocktailId = "11007", // Example ID
        viewModel = MocktailViewModel()
    )
}