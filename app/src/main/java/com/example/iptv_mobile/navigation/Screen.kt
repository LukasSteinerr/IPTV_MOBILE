package com.example.iptv_mobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Tv as TvShowIcon
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Movies : Screen("movies", "Movies", Icons.Default.Movie)
    object TvShows : Screen("tv_shows", "Tv Shows", Icons.Default.TvShowIcon)
    object LiveTv : Screen("live_tv", "Live Tv", Icons.Default.LiveTv)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavScreens = listOf(
    Screen.Movies,
    Screen.TvShows,
    Screen.LiveTv,
    Screen.Settings
)