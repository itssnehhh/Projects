package com.example.storemateproductapplication.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpstoremateapplication.R

import com.example.storemateproductapplication.data.model.FavouriteProduct
import com.example.cpstoremateapplication.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    navController: NavHostController,
    viewModel: ProductViewModel,
) {
    val productList by viewModel.allFavProduct.observeAsState(emptyList())
    var isEnabled by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf<Set<Int>>(emptySet()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF86F804)),
                title = {
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.favourite_screen)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                if (selectedItems.isEmpty()) {
                                    viewModel.deleteFavList(productList)
                                } else {
                                    val selectedProducts = productList.filterIndexed { index, _ -> selectedItems.contains(index) }
                                    viewModel.deleteFavList(selectedProducts)
                                    selectedItems = emptySet()  // Clear selection after deletion
                                }
                            }
                    )
                }
            )
        }
    ) { paddingValues ->

        if (productList.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No data found",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(productList) { product ->
                    FavouriteProductCard(
                        product, viewModel, isEnabled = isEnabled,
                        selectedItem = selectedItems.contains(productList.indexOf(product)),
                        onEnableChange = { value -> isEnabled = value },
                        onClick = {
                            selectedItems =
                                if (selectedItems.contains(productList.indexOf(product))) {
                                    selectedItems.minus(productList.indexOf(product))
                                } else {
                                    selectedItems.plus(productList.indexOf(product))
                                }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavouriteProductCard(
    product: FavouriteProduct,
    viewModel: ProductViewModel,
    isEnabled: Boolean,
    selectedItem: Boolean,
    onEnableChange: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFEAFFEB)),
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape)
            .combinedClickable(
                onLongClick = { onEnableChange(true) },
                onClick = onClick
            )
    ) {

        if (isEnabled)
            Checkbox(
                checked = selectedItem, onCheckedChange = null, modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.End)
            )
        Image(
            painter = rememberAsyncImagePainter(model = product.image),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        )
        Text(
            fontWeight = FontWeight.SemiBold,
            text = product.title,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            fontWeight = FontWeight.SemiBold,
            text = "₹ ${product.price}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
    }
}

