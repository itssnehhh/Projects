package com.example.cpstoremateapplication.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpstoremateapplication.R
import com.example.cpstoremateapplication.ui.screen.Constants.DETAIL_SCREEN
import com.example.cpstoremateapplication.ui.screen.Constants.FAVOURITE_SCREEN
import com.example.cpstoremateapplication.viewModel.ProductViewModel
import com.example.storemateproductapplication.data.model.FavouriteProduct
import com.example.storemateproductapplication.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
) {
    val productList by productViewModel.offlineProductList.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF86F804)),
                title = {
                    Text(
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.app_name)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { navController.navigate(FAVOURITE_SCREEN) }
                            .padding(8.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(productList) { product ->
                ProductCard(product, navController, productViewModel)
            }
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    navController: NavHostController,
    productViewModel: ProductViewModel,
) {
    val context = LocalContext.current
    var isLiked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLiked = productViewModel.isFavorite(product.id)
    }

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFEAFFEB)),
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape)
//            .swipeToDelete {
//                productViewModel.deleteProduct(product)
//            }
            .clickable {
                navController.navigate("$DETAIL_SCREEN/${product.id}")
            }
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = {
                    isLiked = !isLiked
                    val favProduct = FavouriteProduct(
                        productId = product.id,
                        title = product.title,
                        price = product.price,
                        image = product.image
                    )
                    val message = if (isLiked) {
                        productViewModel.insert(favProduct)
                        "Item Added to your favourite"
                    } else {
                        productViewModel.remove(favProduct)
                        "Item removed from your favourite"
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    tint = if (isLiked) Color.Red else Color.Black,
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = product.title,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

