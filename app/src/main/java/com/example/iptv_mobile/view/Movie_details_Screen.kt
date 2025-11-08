package com.example.iptv_mobile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.example.iptv_mobile.model.Movie
import com.example.iptv_mobile.model.Cast
import com.example.iptv_mobile.model.MovieReviewsAndRatings
import com.example.iptv_mobile.repository.TMDBService
import com.example.iptv_mobile.repository.TMDBImageProvider
import com.example.iptv_mobile.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    playlistViewModel: PlaylistViewModel,
    onBack: () -> Unit = {}
) {
    var initialMovie by remember { mutableStateOf<Movie?>(null) }
    var movieDetails by remember { mutableStateOf<Movie?>(null) }
    var cast by remember { mutableStateOf<List<Cast>>(emptyList()) }
    var similarMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var genres by remember { mutableStateOf<List<String>>(emptyList()) }
    var reviewsAndRatings by remember { mutableStateOf<List<MovieReviewsAndRatings>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var backdropUrl by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val tmdbService = remember { TMDBService() }
    val tmdbImageProvider = remember { TMDBImageProvider.getInstance() }

    LaunchedEffect(movieId) {
        coroutineScope.launch {
            // 1. Get initial movie data from local DB
            val localMovie = playlistViewModel.getMovieById(movieId)
            initialMovie = localMovie
            movieDetails = localMovie

            // 2. Fetch TMDB details if tmdbId is available
            localMovie?.tmdbId?.let { tmdbId ->
                try {
                    val details = tmdbService.getMovieDetails(tmdbId)
                    details?.let {
                        movieDetails = localMovie.copy(
                            description = it.optString("overview", localMovie.description ?: ""),
                            rating = it.optDouble("vote_average", 0.0).toString(),
                            duration = it.optInt("runtime", 0).let { if (it > 0) "${it} min" else null }
                        )
                        genres = tmdbService.parseGenres(it)
                    }
                    cast = tmdbService.getMovieCredits(tmdbId)
                    val tmdbSimilarMovies = tmdbService.getSimilarMovies(tmdbId)
                    // NOTE: We skip cross-referencing similar movies with local playlist for now, as PlaylistService.crossReferenceSimilarMovies is not implemented.
                    similarMovies = tmdbSimilarMovies

                    val images = tmdbService.getMovieImages(tmdbId)
                    backdropUrl = tmdbImageProvider.getBackdropUrl(images["backdrop"])

                    // Add mock reviews data similar to external project
                    reviewsAndRatings = listOf(
                        MovieReviewsAndRatings(
                            reviewerName = "FreshTomatoes",
                            reviewerIconUri = "",
                            reviewCount = "250",
                            reviewRating = "92%"
                        ),
                        MovieReviewsAndRatings(
                            reviewerName = "IMDb",
                            reviewerIconUri = "",
                            reviewCount = "1.2k",
                            reviewRating = "8.5"
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Fallback to local data
                }
            }
            isLoading = false
        }
    }

    val displayMovie = movieDetails ?: initialMovie

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(64.dp)
                )
            }
        } else if (displayMovie != null) {
            val currentMovie = displayMovie!!

            // Scrollable content
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    // Movie banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp))
                    ) {
                        MovieImageWithGradients(
                            movieDetails = currentMovie,
                            backdropUrl = backdropUrl ?: currentMovie.coverUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Spacer(Modifier.height(16.dp))

                        // Movie Title + Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = currentMovie.name,
                                    color = Color.White,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = currentMovie.year ?: "Unknown Year",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 15.sp
                                )
                            }

                            currentMovie.rating_5based?.let { rating ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = String.format("%.1f", rating),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        tint = Color.Yellow,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Description
                        currentMovie.description?.let { desc ->
                            ExpandableText(
                                text = desc,
                                collapsedMaxLines = 3
                            )
                        }

                        Spacer(Modifier.height(20.dp))
                    }
                }
            }

            // Close button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
            }

            // Watch Movie Button
            Button(
                onClick = { /* TODO: play movie */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(68.dp)
            ) {
                Text("Watch Movie", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Movie not found.", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun ExpandableText(text: String, collapsedMaxLines: Int) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.8f),
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
        Text(
            text = if (expanded) "Read less" else "Read more",
            color = Color.Red,
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
}

@Composable
private fun MovieImageWithGradients(
    movieDetails: Movie,
    backdropUrl: String?,
    modifier: Modifier = Modifier,
    gradientColor: Color = Color(0xFF0F0F0F), // Match the background color
) {
    val context = LocalContext.current
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(backdropUrl)
                .crossfade(true).build()
        ),
        contentDescription = "Movie poster for ${movieDetails.name}",
        contentScale = ContentScale.Crop,
        modifier = modifier.drawWithContent {
            drawContent()
            drawRect(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, gradientColor),
                    startY = 600f
                )
            )
            drawRect(
                Brush.horizontalGradient(
                    colors = listOf(gradientColor, Color.Transparent),
                    endX = 1000f,
                    startX = 300f
                )
            )
            drawRect(
                Brush.linearGradient(
                    colors = listOf(gradientColor, Color.Transparent),
                    start = Offset(x = 500f, y = 500f),
                    end = Offset(x = 1000f, y = 0f)
                )
            )
        }
    )
}
