package com.example.cpdrinkexplorer.screen

import MocktailViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.cpdrinkexplorer.R
import com.example.cpdrinkexplorer.data.model.Cocktail
import com.example.cpdrinkexplorer.screen.Constants.DETAIL_SCREEN
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavHostController, viewModel: MocktailViewModel) {

    val drinkList by viewModel.mocktails.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.mocktails
    }

    Scaffold { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                SearchBarView { query ->
                    viewModel.searchMocktails(query)
                }
                MocktailList(drinkList) { id ->
                    viewModel.selectMocktail(id)
                    navController.navigate("$DETAIL_SCREEN/$id")
                }
            }
        }
    }
}

@Composable
fun SearchBarView(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("vodka") }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            maxLines = 1,
            value = text,
            onValueChange = { newText ->
                text = newText
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    delay(300) // debounce time
                    if (newText.isNotEmpty()) {
                        onSearch(text)
                    }
                }
            },
            label = { Text(text = "Search") },
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
fun MocktailListItem(mocktail: Cocktail, onClick: () -> Unit) {
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

