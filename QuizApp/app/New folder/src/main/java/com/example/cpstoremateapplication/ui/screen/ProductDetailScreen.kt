package com.example.cpstoremateapplication.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cpstoremateapplication.data.model.FavouriteProduct
import com.example.cpstoremateapplication.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    productId: Int?,
) {

    val product by productViewModel.getProductDetail(productId!!).observeAsState()
    var message by remember { mutableStateOf("") }
    var isLiked by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLiked = productViewModel.isFavorite(product!!.id)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (productId != null) {
            productViewModel.getProductDetail(productId)
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFACF55A)),
                    onClick = {
                        val favProduct = product?.let { product ->
                            FavouriteProduct(
                                productId = product.id,
                                title = product.title,
                                price = product.price,
                                image = product.image
                            )
                        }
                        if (isLiked) {
                            productViewModel.remove(favProduct!!)
                            message = "Item removed from your favourite"
                        } else {
                            productViewModel.insert(favProduct!!)
                            message = "Item Added to your favourite"
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Delete else Icons.Default.Favorite,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Text(
                        text = if (isLiked) "Remove Favorite" else "Add to Favorite",
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp),
                        color = Color.Black
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFACF55A)),
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "",
                        tint = Color.Black
                    )
                    Text(
                        text = "Add to Cart",
                        modifier = Modifier.padding(4.dp),
                        color = Color.Black
                    )
                }
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                product?.let { product ->
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(Color(0xFF86F804)),
                        title = {
                            Text(
                                text = product.category.replaceFirst(
                                    product.category[0],
                                    product.category[0].titlecaseChar()
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                            )
                        })
                    Image(
                        contentScale = ContentScale.Fit,
                        painter = rememberAsyncImagePainter(model = product.image),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "₹ ${product.price}",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                        )
                        StarRatingBar(rating = product.rating.rate)
                        Text(
                            text = "(${product.rating.count})",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )

                }
            }
        }
    }
}

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
) {
    val density = LocalDensity.current.density
    val starSize = (8f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0xFFFCEBB4)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}
