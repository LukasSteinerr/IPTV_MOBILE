package com.example.iptv_mobile.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.repository.PlaylistService
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistService: PlaylistService) : ViewModel() {
    val isLoading = mutableStateOf(false)
    val loadingMessage = mutableStateOf("")
    val errorMessage = mutableStateOf("")

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""
            try {
                playlistService.addPlaylist(playlist) { message ->
                    loadingMessage.value = message
                }
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
        }
    }
}