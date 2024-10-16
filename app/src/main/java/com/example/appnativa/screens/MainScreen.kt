package com.example.appnativa.screens
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigatorDestinationBuilder
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appnativa.BottomNavGraph
import com.example.appnativa.components.BottomBarScreen
import com.example.compose.AppnativaTheme
import com.example.compose.surfaceContainerHighDarkMediumContrast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home // Otros íconos que necesites
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    AppnativaTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Retro Shop / Admin") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("logOut")
                        }) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                BottomNavGraph(navController = navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
   MainScreen()
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavController
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    NavigationBarItem(
        selected = selected,
        onClick = { navController.navigate(screen.route) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation icon",
                tint = if (selected) Color.Yellow else Color.Gray
            )
        },
        label = {
            Text(
                text = screen.title,
                color = if (selected) Color.Yellow else Color.Gray
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Yellow, // Color del ícono seleccionado
            unselectedIconColor = Color.Gray, // Color del ícono no seleccionado
            selectedTextColor = Color.Yellow, // Color del texto seleccionado
            unselectedTextColor = Color.Gray // Color del texto no seleccionado
        ),
        modifier = Modifier
            .background(Color.Transparent)
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.ListProducts,
        BottomBarScreen.ShoppingCart,
        BottomBarScreen.Customers
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .background(surfaceContainerHighDarkMediumContrast)
            .padding(0.dp)
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}


