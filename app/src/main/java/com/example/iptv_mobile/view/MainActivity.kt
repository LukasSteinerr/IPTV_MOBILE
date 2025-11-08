package com.example.iptv_mobile.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iptv_mobile.repository.PlaylistService
import com.example.iptv_mobile.viewmodel.PlaylistViewModel
import com.example.iptv_mobile.viewmodel.PlaylistViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistService = PlaylistService()
        val hasPlaylists = playlistService.hasPlaylists()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModelFactory(playlistService))
                    val playlists by playlistViewModel.playlists.collectAsState()
                    
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            // Use local state to manage navigation
                            var showAddPlaylist by remember { mutableStateOf(hasPlaylists.not()) }
                            var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }

                            when (showAddPlaylist) {
                                true -> {
                                    AddPlaylistScreen(
                                        playlistService = playlistService,
                                        onPlaylistAdded = {
                                            playlistViewModel.loadPlaylists()
                                            showAddPlaylist = false
                                        }
                                    )
                                }
                                false -> {
                                    MainContentScreen(
                                        playlists = playlists,
                                        playlistViewModel = playlistViewModel,
                                        selectedPlaylist = selectedPlaylist,
                                        onPlaylistClick = { playlist ->
                                            selectedPlaylist = playlist
                                        },
                                        navController = navController
                                    )
                                }
                            }
                        }
                        composable(
                            route = Screen.MovieDetails.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.LongType })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getLong("movieId") ?: return@composable
                            MovieDetailsScreen(
                                movieId = movieId,
                                playlistViewModel = playlistViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}