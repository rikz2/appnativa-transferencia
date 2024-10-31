package com.example.appnativa.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appnativa.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import com.example.appnativa.models.ProductCardModel
import com.example.appnativa.models.ProductCardStatus
import com.example.appnativa.onLoginSuccess
import com.example.compose.AppnativaTheme
import com.example.compose.errorDark
import com.example.compose.onErrorDark
import com.example.compose.onPrimaryContainerDark
import com.example.compose.onSecondaryDark
import com.example.compose.secondaryContainerDark
import com.example.compose.surfaceContainerDark
import coil.compose.rememberImagePainter
import com.example.appnativa.service.ProductService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProductCard(
    imageUrl: String,
    title: String,
    description: String,
    price: String,
    status: ProductCardStatus,
    onAddToCartClick: () -> Unit,
    onDeleteClickShoppingCart: () -> Unit,
    onDeleteClickActive: () -> Unit,
    onUpdateClick: () -> Unit // Callback para la opción de actualización
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(surfaceContainerDark)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                if (status == ProductCardStatus.ACTIVE) {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Actualizar") },
                            onClick = {
                                showMenu = false
                                onUpdateClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = price,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (status == ProductCardStatus.SHOPPINGCART) {
                    CustomButton(
                        text = "Eliminar",
                        onClick = onDeleteClickShoppingCart,
                        modifier = Modifier.height(50.dp),
                        backgroundColor = Color.Transparent,
                        contentColor = errorDark,
                        fontSize = 18,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (status == ProductCardStatus.ACTIVE) {
                    Row {
                        CustomButton(
                            text = "Eliminar",
                            onClick = onDeleteClickActive,
                            modifier = Modifier.height(50.dp),
                            backgroundColor = Color.Transparent,
                            contentColor = Color.Red,
                            fontSize = 18,
                            fontWeight = FontWeight.Bold
                        )
                        CustomButton(
                            text = "Agregar al Carrito",
                            onClick = onAddToCartClick,
                            modifier = Modifier.height(50.dp),
                            backgroundColor = onPrimaryContainerDark,
                            contentColor = Color.Black,
                            fontSize = 18,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    fontSize: Int,
    fontWeight: FontWeight = FontWeight.Normal
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(backgroundColor)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize.sp,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun ListProductCard(status: ProductCardStatus, productService: ProductService, user: FirebaseUser?) {
    val productsState = remember { mutableStateOf<List<ProductCardModel>>(emptyList()) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var productToUpdate by remember { mutableStateOf<ProductCardModel?>(null) }

    LaunchedEffect(Unit) {
        loadProductsForUser(user, productService, productsState, status)
    }

    val products = productsState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        if (products.isEmpty()) {
            Text("No se encontraron productos.", color = Color.White)
        } else {
            products.forEach { item ->
                ProductCard(
                    imageUrl = item.imageUrl,
                    title = item.title,
                    description = item.description,
                    price = item.price,
                    status = item.status,
                    onAddToCartClick = {
                        productService.updateProductStatus(item.id, ProductCardStatus.SHOPPINGCART) { success ->
                            if (success) {
                                loadProductsForUser(user, productService, productsState, status)
                            } else {
                                // Maneja el error
                            }
                        }
                    },
                    onDeleteClickShoppingCart = {
                        productService.updateProductStatus(item.id, ProductCardStatus.ACTIVE) { success ->
                            if (success) {
                                loadProductsForUser(user, productService, productsState, status)
                            } else {
                                // Maneja el error
                            }
                        }
                    },
                    onDeleteClickActive = {
                        productService.deleteProduct(item.id) { success ->
                            if (success) loadProductsForUser(user, productService, productsState, status)
                        }
                    },
                    onUpdateClick = {
                        productToUpdate = item
                        showUpdateDialog = true
                    }
                )
            }
        }
    }

    if (showUpdateDialog && productToUpdate != null) {
        UpdateProductDialog(
            product = productToUpdate!!,
            onDismiss = { showUpdateDialog = false },
            onUpdate = { updatedProduct ->
                productService.updateProduct(updatedProduct) { success ->
                    if (success) loadProductsForUser(user, productService, productsState, status)
                    showUpdateDialog = false
                }
            }
        )
    }
}

@Composable
fun UpdateProductDialog(
    product: ProductCardModel,
    onDismiss: () -> Unit,
    onUpdate: (ProductCardModel) -> Unit
) {
    var imageUrl by remember { mutableStateOf(product.imageUrl) }
    var title by remember { mutableStateOf(product.title) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Producto", fontWeight = FontWeight.Bold)},
        text = {
            Column {
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL de la imagen") })
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdate(product.copy(imageUrl = imageUrl, title = title, description = description, price = price))
            }) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Red)
            }
        }
    )
}



// Función auxiliar para cargar productos filtrados por estado y usuario
private fun loadProductsForUser(
    user: FirebaseUser?,
    productService: ProductService,
    productsState: MutableState<List<ProductCardModel>>,
    status: ProductCardStatus
) {
    val userId = user?.uid
    if (userId != null) {
        productService.getProductsByUser { userProducts ->
            productsState.value = userProducts.filter { it.status == status && it.uid == userId }
        }
    }
}



