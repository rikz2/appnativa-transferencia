package com.example.appnativa

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appnativa.components.BottomBarScreen
import com.example.appnativa.screens.CustomersScreen
import com.example.appnativa.screens.Home
import com.example.appnativa.screens.ShoppingCart
import com.example.appnativa.screens.ListProducts
import com.example.appnativa.screens.MainScreen
import com.google.firebase.auth.FirebaseUser

@Composable
fun BottomNavGraph(navController: NavHostController, user:FirebaseUser?) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            Home(user)
        }
        composable(route = BottomBarScreen.ListProducts.route) {
            ListProducts(user)
        }
        composable(route = BottomBarScreen.ShoppingCart.route) {
            ShoppingCart(user)
        }

        composable(route = BottomBarScreen.Customers.route) {
            CustomersScreen()
        }
        composable(route = "main_screen") {
            MainScreen()  // Aquí se navega a MainScreen
        }

        // Definir la ruta de logOut
        composable(route = "logOut") {
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }

    }
}
