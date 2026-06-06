package com.example.quacksports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quacksports.model.Reservation
import com.example.quacksports.model.UserRole
import com.example.quacksports.ui.screens.HomeScreen
import com.example.quacksports.ui.screens.LoginScreen
import com.example.quacksports.ui.screens.MapScreen
import com.example.quacksports.ui.screens.ProfileScreen
import com.example.quacksports.ui.screens.RegisterScreen
import com.example.quacksports.ui.screens.ReservationsScreen
import com.example.quacksports.ui.screens.admin.AdminDashboardScreen
import com.example.quacksports.ui.screens.company.CompanyDashboardScreen
import com.example.quacksports.ui.screens.company.CompanyReservationsScreen
import com.example.quacksports.ui.screens.company.ManageCourtScreen
import com.example.quacksports.ui.screens.company.ManageVenuesScreen
import com.example.quacksports.ui.screens.dev.SeedScreen
import com.example.quacksports.ui.screens.user.AddressesScreen
import com.example.quacksports.ui.screens.user.CardsScreen
import com.example.quacksports.ui.screens.user.CourtBookingScreen
import com.example.quacksports.ui.screens.user.PaymentScreen
import com.example.quacksports.ui.screens.user.VenueDetailScreen
import com.example.quacksports.ui.theme.QUACKSPORTSTheme
import com.example.quacksports.ui.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

object PaymentDraftHolder { var draft: Reservation? = null }

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

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Explore : Screen("explore", "Explorar", Icons.Filled.Search)
    object Favorites : Screen("favorites", "Favoritos", Icons.Filled.FavoriteBorder)
    object Trips : Screen("reservations", "Reservas", Icons.AutoMirrored.Filled.Send)
    object Profile : Screen("profile", "Perfil", Icons.Filled.AccountCircle)
}

val bottomNavItems = listOf(Screen.Explore, Screen.Favorites, Screen.Trips, Screen.Profile)

/** Sends the user to their role landing and clears the auth stack behind them. */
private fun routeByRole(navController: NavHostController, role: UserRole) {
    val dest = when (role) {
        UserRole.ADMIN -> "admin_dashboard"
        UserRole.COMPANY -> "company_dashboard"
        UserRole.USER -> Screen.Explore.route
    }
    navController.navigate(dest) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

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
                NavigationBar(containerColor = Color.White) {
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
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE51D53),
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
        val startDestination = if (authViewModel.isLoggedIn()) Screen.Explore.route else "login"
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { routeByRole(navController, authViewModel.role) },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = { routeByRole(navController, authViewModel.role) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Explore.route) {
                HomeScreen(
                    onNavigateToDetail = { id -> navController.navigate("detail/$id") },
                    onNavigateToMap = { navController.navigate("map") }
                )
            }
            composable("map") {
                MapScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToDetail = { id -> navController.navigate("detail/$id") }
                )
            }
            composable(Screen.Favorites.route) { Text("Favoritos", modifier = Modifier.padding(16.dp)) }
            composable(Screen.Trips.route) { ReservationsScreen(onBack = { navController.popBackStack() }) }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = { navController.navigate("login") { popUpTo(0) { inclusive = true } } },
                    onNavigateToReservations = { navController.navigate(Screen.Trips.route) },
                    onNavigateToAddresses = { navController.navigate("addresses") },
                    onNavigateToCards = { navController.navigate("cards") },
                    onNavigateToCompany = { navController.navigate("company_dashboard") },
                    onNavigateToAdmin = { navController.navigate("admin_dashboard") },
                    onNavigateToSeed = { navController.navigate("seed") },
                    authViewModel = authViewModel
                )
            }
            composable("detail/{venueId}") { backStackEntry ->
                VenueDetailScreen(
                    venueId = backStackEntry.arguments?.getString("venueId") ?: "",
                    onBack = { navController.popBackStack() },
                    onCourtSelected = { v, c -> navController.navigate("booking/$v/$c") }
                )
            }
            composable("booking/{venueId}/{courtId}") { backStackEntry ->
                CourtBookingScreen(
                    venueId = backStackEntry.arguments?.getString("venueId") ?: "",
                    courtId = backStackEntry.arguments?.getString("courtId") ?: "",
                    onBack = { navController.popBackStack() },
                    onProceedToPayment = { draft ->
                        if (!authViewModel.isLoggedIn()) {
                            navController.navigate("login")
                        } else {
                            PaymentDraftHolder.draft = draft
                            navController.navigate("payment")
                        }
                    }
                )
            }
            composable("payment") {
                val draft = PaymentDraftHolder.draft
                if (draft == null) {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                } else {
                    PaymentScreen(
                        draft = draft,
                        onBack = { navController.popBackStack() },
                        onPaid = { navController.navigate(Screen.Trips.route) { popUpTo(Screen.Explore.route) } },
                        onAddCard = { navController.navigate("cards") }
                    )
                }
            }
            composable("addresses") { AddressesScreen(onBack = { navController.popBackStack() }) }
            composable("cards") { CardsScreen(onBack = { navController.popBackStack() }) }
            composable("seed") { SeedScreen(onBack = { navController.popBackStack() }, authViewModel = authViewModel) }
            composable("company_dashboard") {
                val cid = authViewModel.currentUserData?.companyId ?: ""
                CompanyDashboardScreen(
                    companyId = cid,
                    onBack = { navController.popBackStack() },
                    onManageVenues = { navController.navigate("manage_venues") },
                    onViewReservations = { navController.navigate("company_reservations") }
                )
            }
            composable("manage_venues") {
                val cid = authViewModel.currentUserData?.companyId ?: ""
                ManageVenuesScreen(
                    companyId = cid,
                    onBack = { navController.popBackStack() },
                    onEditCourts = { venueId -> navController.navigate("manage_court/$venueId") }
                )
            }
            composable("manage_court/{venueId}") { backStackEntry ->
                ManageCourtScreen(
                    venueId = backStackEntry.arguments?.getString("venueId") ?: "",
                    onBack = { navController.popBackStack() }
                )
            }
            composable("company_reservations") {
                val cid = authViewModel.currentUserData?.companyId ?: ""
                CompanyReservationsScreen(companyId = cid, onBack = { navController.popBackStack() })
            }
            composable("admin_dashboard") { AdminDashboardScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
