package com.example.iptv_mobile.repository

import com.example.iptv_mobile.model.Cast
import com.example.iptv_mobile.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TMDBService {
    private val client = OkHttpClient()
    private val apiKey = "9e92699e050cb40728b59728c3115455" // TODO: Replace with actual API key

    private fun getTmdbUrl(path: String, params: Map<String, String> = emptyMap()): String {
        val urlBuilder = StringBuilder("https://api.themoviedb.org/3/$path?api_key=$apiKey")
        params.forEach { (key, value) ->
            urlBuilder.append("&$key=$value")
        }
        return urlBuilder.toString()
    }

    private suspend fun fetchJson(url: String): JSONObject? = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonString = response.body?.string()
                return@withContext jsonString?.let { JSONObject(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    suspend fun getMovieDetails(tmdbId: String): JSONObject? {
        return fetchJson(getTmdbUrl("movie/$tmdbId"))
    }

    fun parseGenres(details: JSONObject): List<String> {
        val genresArray = details.optJSONArray("genres") ?: return emptyList()
        val genres = mutableListOf<String>()
        for (i in 0 until genresArray.length()) {
            genres.add(genresArray.getJSONObject(i).getString("name"))
        }
        return genres
    }

    suspend fun getMovieCredits(tmdbId: String): List<Cast> {
        val json = fetchJson(getTmdbUrl("movie/$tmdbId/credits")) ?: return emptyList()
        val castList = mutableListOf<Cast>()
        val castArray = json.optJSONArray("cast") ?: return emptyList()

        for (i in 0 until castArray.length()) {
            val castObject = castArray.getJSONObject(i)
            castList.add(
                Cast(
                    name = castObject.optString("name", ""),
                    character = castObject.optString("character", ""),
                    profilePath = castObject.optString("profile_path", null)
                )
            )
        }
        return castList
    }

    suspend fun getSimilarMovies(tmdbId: String): List<Movie> {
        val json = fetchJson(getTmdbUrl("movie/$tmdbId/similar")) ?: return emptyList()
        val movies = mutableListOf<Movie>()
        val resultsArray = json.optJSONArray("results") ?: return emptyList()

        for (i in 0 until resultsArray.length()) {
            val movieObject = resultsArray.getJSONObject(i)
            movies.add(
                Movie(
                    tmdbId = movieObject.optString("id", null),
                    name = movieObject.optString("title", ""),
                    posterUrl = movieObject.optString("poster_path", null),
                    backdropUrl = movieObject.optString("backdrop_path", null),
                    description = movieObject.optString("overview", null),
                    rating = movieObject.optDouble("vote_average", 0.0).toString(),
                    year = movieObject.optString("release_date", null)?.substringBefore("-")
                )
            )
        }
        return movies
    }

    suspend fun getMovieImages(tmdbId: String): Map<String, String?> {
        val json = fetchJson(getTmdbUrl("movie/$tmdbId/images")) ?: return emptyMap()
        val images = mutableMapOf<String, String?>()

        // Assuming the external project only needs poster and backdrop paths
        val postersArray = json.optJSONArray("posters")
        if (postersArray != null && postersArray.length() > 0) {
            images["poster"] = postersArray.getJSONObject(0).optString("file_path", null)
        }

        val backdropsArray = json.optJSONArray("backdrops")
        if (backdropsArray != null && backdropsArray.length() > 0) {
            images["backdrop"] = backdropsArray.getJSONObject(0).optString("file_path", null)
        }

        return images
    }
}