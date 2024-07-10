package com.example.universitydirectoryapplication.ui.screen.search

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.universitydirectoryapplication.R
import com.example.universitydirectoryapplication.data.model.University
import com.example.universitydirectoryapplication.ui.screen.Constant
import com.example.universitydirectoryapplication.ui.theme.CustomLightGrey
import com.example.universitydirectoryapplication.ui.theme.CustomRed
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {

    val context = LocalContext.current
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val isLoading by searchViewModel.isLoading.collectAsState()

    val searchedText by searchViewModel.searchText.collectAsState()
    val countryList = remember {
        context.resources.getStringArray(R.array.countryList)
    }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchList by searchViewModel.searchUniversity.collectAsState()

    LaunchedEffect(Unit) {
        searchViewModel.searchUniversity
    }

    Column(modifier = Modifier.background(CustomRed)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                colors = SearchBarDefaults.colors(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                },
                shape = SearchBarDefaults.dockedShape,
                trailingIcon = {
                    Box {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { expanded = true }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        ) {
                            countryList.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(text = type, fontSize = 16.sp) },
                                    onClick = {
                                        searchViewModel.onSearchedCountryChange(type)
                                        searchViewModel.onSearchedTextChange(type)
                                        expanded = false
                                        searchViewModel.searchUniversity(type)
                                    }
                                )
                            }
                        }
                    }
                },
                query = searchedText,
                onQueryChange = { searchViewModel.onSearchedTextChange(it) },
                onSearch = { searchViewModel.searchUniversity(searchedText) },
                active = false,
                onActiveChange = {},
                content = {}
            )
        }

        if (isLoading) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                items(searchList) { university ->
                    UniversityCard(university = university, navController = navController)
                }
            }
        }
    }

    if (searchList.isEmpty() && !isLoading) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No Data Found.")
        }
    }
}

@Composable
fun UniversityCard(university: University, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, CardDefaults.shape)
            .clickable {
                if (university.web_pages.isNotEmpty()) {
                    val encodedUrl = URLEncoder.encode(
                        university.web_pages[0],
                        StandardCharsets.UTF_8.toString()
                    )
                    navController.navigate("${Constant.WEB_SCREEN}/$encodedUrl")
                }
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(CustomLightGrey)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(
                colors = CardDefaults.cardColors(Color.LightGray),
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(60.dp)
                    .border(1.dp, Color.Black, CircleShape)
            ) {
                Text(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    text = university.name.take(2).uppercase(Locale.ROOT),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = university.name,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = stringResource(id = R.string.pin, university.country),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )
                Text(
                    text = university.web_pages.toString(),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}