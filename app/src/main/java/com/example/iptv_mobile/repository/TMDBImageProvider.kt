package com.example.iptv_mobile.repository

class TMDBImageProvider private constructor() {

    companion object {
        private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
        private const val POSTER_SIZE = "w342"
        private const val BACKDROP_SIZE = "w1280"
        private const val PROFILE_SIZE = "w185"

        @Volatile
        private var instance: TMDBImageProvider? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TMDBImageProvider().also { instance = it }
            }
    }

    fun getPosterUrl(path: String?): String? {
        return path?.let { BASE_IMAGE_URL + POSTER_SIZE + it }
    }

    fun getBackdropUrl(path: String?): String? {
        return path?.let { BASE_IMAGE_URL + BACKDROP_SIZE + it }
    }

    fun getProfileUrl(path: String?): String? {
        return path?.let { BASE_IMAGE_URL + PROFILE_SIZE + it }
    }
}