package com.example.iptv_mobile.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iptv_mobile.navigation.Screen
import com.example.iptv_mobile.navigation.bottomNavScreens
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.repository.PlaylistService

@Composable
fun MainContentScreen(
    playlists: List<Playlist>,
    playlistService: PlaylistService,
    selectedPlaylist: Playlist?,
    onPlaylistClick: (Playlist) -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        MainContentNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            playlists = playlists,
            playlistService = playlistService,
            selectedPlaylist = selectedPlaylist,
            onPlaylistClick = onPlaylistClick
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFF121212), // Dark background matching the image
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title,
                        tint = Color.White // All icons are white
                    )
                },
                label = { /* Removed label to match the icon-only style in the image */ },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Avoid building up a large stack of destinations on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun MainContentNavHost(
    navController: NavHostController,
    modifier: Modifier,
    playlists: List<Playlist>,
    playlistService: PlaylistService,
    selectedPlaylist: Playlist?,
    onPlaylistClick: (Playlist) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { MoviesScreen() }
        composable(Screen.Downloads.route) { DownloadsScreen() }
        composable(Screen.MyList.route) { FavoritesScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}