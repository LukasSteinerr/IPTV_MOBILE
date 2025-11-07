package com.example.iptv_mobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    // Mapping existing routes to new visual icons from the image
    object Movies : Screen("movies", "Movies", Icons.Filled.Home, Icons.Outlined.Home)
    object TvShows : Screen("tv_shows", "Tv Shows", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object LiveTv : Screen("live_tv", "Live Tv", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    object Settings : Screen("settings", "Settings", Icons.Filled.Tune, Icons.Outlined.Tune)
}

val bottomNavScreens = listOf(
    Screen.Movies,
    Screen.TvShows,
    Screen.LiveTv,
    Screen.Settings
)