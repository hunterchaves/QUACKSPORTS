package com.example.quacksports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quacksports.ui.screens.DetailScreen
import com.example.quacksports.ui.screens.HomeScreen
import com.example.quacksports.ui.screens.LoginScreen
import com.example.quacksports.ui.screens.RegisterScreen
import com.example.quacksports.ui.screens.MapScreen
import com.example.quacksports.ui.screens.ProfileScreen
import com.example.quacksports.ui.screens.ReservationsScreen
import com.example.quacksports.ui.theme.QUACKSPORTSTheme
import com.example.quacksports.ui.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            QUACKSPORTSTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Explore : Screen("explore", "Explorar", Icons.Filled.Search)
    object Favorites : Screen("favorites", "Favoritos", Icons.Filled.FavoriteBorder)
    object Trips : Screen("reservations", "Reservas", Icons.AutoMirrored.Filled.Send) // Placeholder icon
    object Profile : Screen("profile", "Perfil", Icons.Filled.AccountCircle)
}

val bottomNavItems = listOf(
    Screen.Explore,
    Screen.Favorites,
    Screen.Trips,
    Screen.Profile
)

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }
            
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                if ((screen == Screen.Trips || screen == Screen.Profile) && !authViewModel.isLoggedIn()) {
                                    navController.navigate("login")
                                } else {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE51D53), // Quackish pinkish red
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = Color.Black,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Explore.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.popBackStack()
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.popBackStack("login", inclusive = true)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.Explore.route) {
                HomeScreen(
                    onNavigateToDetail = { id ->
                        navController.navigate("detail/$id")
                    },
                    onNavigateToMap = {
                        navController.navigate("map")
                    }
                )
            }
            composable("map") {
                MapScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToDetail = { id ->
                        navController.navigate("detail/$id")
                    }
                )
            }
            composable(Screen.Favorites.route) { Text("Favoritos", modifier = Modifier.padding(16.dp)) }
            composable(Screen.Trips.route) {
                ReservationsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Explore.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToReservations = {
                        navController.navigate(Screen.Trips.route)
                    }
                )
            }
            
            composable("detail/{venueId}") { backStackEntry ->
                val venueId = backStackEntry.arguments?.getString("venueId")
                DetailScreen(venueId = venueId, onBack = { navController.popBackStack() })
            }
        }
    }
}
