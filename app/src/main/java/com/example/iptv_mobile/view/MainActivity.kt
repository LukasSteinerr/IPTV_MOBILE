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
                    
                    // Use a local state to manage navigation after initial check
                    var showAddPlaylist by remember { mutableStateOf(hasPlaylists.not()) }

                    if (showAddPlaylist) {
                        AddPlaylistScreen(
                            playlistService = playlistService,
                            onPlaylistAdded = {
                                playlistViewModel.loadPlaylists()
                                showAddPlaylist = false
                            }
                        )
                    } else {
                        MyPlaylistsScreen(
                            playlists = playlists,
                            playlistService = playlistService
                        )
                    }
                }
            }
        }
    }
}