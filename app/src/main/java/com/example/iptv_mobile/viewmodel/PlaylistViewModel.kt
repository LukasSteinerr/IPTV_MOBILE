package com.example.iptv_mobile.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.repository.PlaylistService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.iptv_mobile.model.Category
import com.example.iptv_mobile.model.Movie

class PlaylistViewModel(private val playlistService: PlaylistService) : ViewModel() {
    val isLoading = mutableStateOf(false)
    val loadingMessage = mutableStateOf("")
    val errorMessage = mutableStateOf("")

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    init {
        loadPlaylists()
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value = playlistService.getAllPlaylists()
        }
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""
            try {
                playlistService.addPlaylist(playlist) { message ->
                    loadingMessage.value = message
                }
                loadPlaylists() // Refresh navigation state after adding
            } catch (e: Exception) {
                errorMessage.value = "Failed to add playlist: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            playlistService.clearDatabase()
            loadPlaylists() // Refresh navigation state after clearing
        }
    }

    suspend fun getCategoriesForPlaylist(playlistId: Long): List<Category> {
        return playlistService.getCategoriesForPlaylist(playlistId)
    }

    suspend fun getMoviesForCategory(categoryId: Long): List<Movie> {
        return playlistService.getMoviesForCategory(categoryId)
    }
}
