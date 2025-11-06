package com.example.iptv_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.iptv_mobile.repository.PlaylistService

class PlaylistViewModelFactory(private val playlistService: PlaylistService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(playlistService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}