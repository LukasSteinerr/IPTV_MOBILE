package com.example.iptv_mobile.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.repository.PlaylistService
import com.example.iptv_mobile.viewmodel.PlaylistViewModel
import com.example.iptv_mobile.viewmodel.PlaylistViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlaylistsScreen(
    playlists: List<Playlist>,
    playlistService: PlaylistService,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModelFactory(playlistService))
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Playlists") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(playlists) { playlist ->
                PlaylistItem(playlist = playlist)
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist) {
    Column {
        Text(text = playlist.name)
        Text(text = playlist.url)
    }
}