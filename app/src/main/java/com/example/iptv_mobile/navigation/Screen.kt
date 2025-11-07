package com.example.iptv_mobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    // New navigation screens
    object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Downloads : Screen("downloads", "Downloads", Icons.Filled.Download, Icons.Outlined.Download)
    object MyList : Screen("my_list", "Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Settings : Screen("settings", "Settings", Icons.Filled.Tune, Icons.Outlined.Tune)

    // App Content Screens
    object Movies : Screen("movies", "Movies", Icons.Filled.MovieCreation, Icons.Outlined.MovieCreation)
    object TvShows : Screen("tv_shows", "Tv Shows", Icons.Filled.Subscriptions, Icons.Outlined.Subscriptions)
    object LiveTv : Screen("live_tv", "Live TV", Icons.Filled.Sensors, Icons.Outlined.Sensors)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Downloads,
    Screen.MyList,
    Screen.Settings
)