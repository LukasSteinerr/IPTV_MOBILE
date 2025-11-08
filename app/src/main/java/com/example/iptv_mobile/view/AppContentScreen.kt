package com.example.iptv_mobile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iptv_mobile.navigation.Screen

import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.viewmodel.PlaylistViewModel

@Composable
fun AppContentScreen(
    playlists: List<Playlist>,
    playlistViewModel: PlaylistViewModel
) {
    val navController = rememberNavController()
    val selectedPlaylist = playlists.firstOrNull() // Or however you want to select the playlist

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Spacer to replace TopBar height
        Spacer(Modifier.height(12.dp))

        // --- Tabs and Search Icon ---
        TopAppBar(navController = navController)

        Spacer(Modifier.height(16.dp))

        if (selectedPlaylist != null) {
            AppContentNavHost(
                navController = navController,
                playlist = selectedPlaylist,
                playlistViewModel = playlistViewModel
            )
        } else {
            // Handle case where there are no playlists
        }
    }
}

@Composable
fun TopAppBar(navController: NavHostController) {
    val tabs = listOf(
        Screen.Movies,
        Screen.TvShows,
        Screen.LiveTv
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tabs.forEach { screen ->
            val selected = currentRoute == screen.route
            Text(
                text = screen.title,
                color = if (selected) Color.White else Color.Gray,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.clickable {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        IconButton(onClick = { /* Handle search click */ }) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
        }
    }
}

@Composable
fun AppContentNavHost(
    navController: NavHostController,
    playlist: Playlist,
    playlistViewModel: PlaylistViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Movies.route
    ) {
        composable(Screen.Movies.route) {
            MoviesScreen(
                playlist = playlist,
                playlistViewModel = playlistViewModel
            )
        }
        composable(Screen.TvShows.route) { TvShowsScreen() }
        composable(Screen.LiveTv.route) { LiveTvScreen() }
    }
}
