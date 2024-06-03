package com.example.cpdrinkexplorer

import MocktailViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.cpdrinkexplorer.screen.MainScreen
import com.example.cpdrinkexplorer.data.model.Cocktail

class MainActivity : ComponentActivity() {

    private val viewModel: MocktailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel)
        }
    }
}

@Composable
fun MocktailApp(viewModel: MocktailViewModel) {
    val mocktails by viewModel.mocktails.collectAsState()
    val selectedMocktail by viewModel.selectedMocktail.collectAsState()
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar { query ->
                viewModel.searchMocktails(query)
            }
            MocktailList(mocktails) { id ->
                viewModel.selectMocktail(id)
            }
            selectedMocktail?.let { mocktail ->
                MocktailDetail(mocktail)
            }
        }
    }
}

@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("vodka") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
    }
}

@Composable
fun MocktailList(mocktails: List<Cocktail>, onItemClick: (String) -> Unit) {
    Column {
        mocktails.forEach { mocktail ->
            MocktailListItem(mocktail) {
                onItemClick(mocktail.idDrink)
            }
        }
    }
}

@Composable
private fun MocktailListItem(mocktail: Cocktail, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }) {
        Image(
            painter = rememberImagePainter(
                data = mocktail.strDrinkThumb ?: R.drawable.ic_launcher_background
            ),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = mocktail.strDrink, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun MocktailDetail(mocktail: Cocktail) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = mocktail.strDrink, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Ingredients:", style = MaterialTheme.typography.headlineSmall)
        Text(text = "- ${mocktail.strIngredient1}")
        Text(text = "- ${mocktail.strIngredient2}")
        Text(text = "- ${mocktail.strIngredient3}")
        Text(text = "- ${mocktail.strIngredient4}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Instructions:", style = MaterialTheme.typography.headlineMedium)
        Text(text = mocktail.strInstructions)
    }
}
