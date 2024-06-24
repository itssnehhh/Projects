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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpstoremateapplication.R
import com.example.cpstoremateapplication.data.model.FavouriteProduct
import com.example.cpstoremateapplication.data.model.Product
import com.example.cpstoremateapplication.ui.components.ProductItem
import com.example.cpstoremateapplication.ui.components.PullToRefreshLazyColumn
import com.example.cpstoremateapplication.ui.screen.Constants.DETAIL_SCREEN
import com.example.cpstoremateapplication.ui.screen.Constants.FAVOURITE_SCREEN
import com.example.cpstoremateapplication.viewModel.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        var isRefreshing by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            PullToRefreshLazyColumn(
                items = productList,
                content = { product ->
                    ProductItem(product, navController, productViewModel)
                },
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        productViewModel.getProducts()
                        delay(1000)
                        isRefreshing = false
                    }
                }, viewModel = productViewModel
            )
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
    var message by remember { mutableStateOf("") }

    LaunchedEffect(product.id) {
        isLiked = productViewModel.isFavorite(product.id)
    }

    val iconImage = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFEAFFEB)),
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.LightGray, CardDefaults.shape)
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
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .offset(y = 144.dp, x = 144.dp)
                    .clickable {
                        val favProduct = FavouriteProduct(
                            productId = product.id,
                            title = product.title,
                            price = product.price,
                            image = product.image
                        )
                        message = if (isLiked) {
                            productViewModel.remove(favProduct)
                            "Item removed to your favourite"
                        } else {
                            productViewModel.insert(favProduct)
                            "Item insert to your favourite"
                        }
                        isLiked = !isLiked
                        Toast
                            .makeText(context, message, Toast.LENGTH_SHORT)
                            .show()
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Icon(
                    tint = if (isLiked) Color.Red else Color.Black,
                    imageVector = iconImage,
                    contentDescription = "",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        Text(
            fontWeight = FontWeight.SemiBold,
            text = product.title,
            minLines = 2,
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}
