package com.example.appnativa.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title:String,
    val icon:ImageVector
) {
    object Home: BottomBarScreen(
        route = "Home",
        title="Home",
        icon= Icons.Default.Home
    )
    object ListProducts: BottomBarScreen(
        route = "ListProducts",
        title="Productos",
        icon= Icons.Default.Star
    )

    object ShoppingCart: BottomBarScreen(
        route = "ShoppingCart",
        title="Carrito",
        icon= Icons.Default.ShoppingCart
    )

    object Customers: BottomBarScreen(
        route = "Customers",
        title="Clientes",
        icon= Icons.Default.AccountCircle
    )
}
