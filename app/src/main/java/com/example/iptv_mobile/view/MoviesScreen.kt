package com.example.iptv_mobile.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController

import com.example.iptv_mobile.model.Category
import com.example.iptv_mobile.repository.TMDBImageProvider
import com.example.iptv_mobile.view.components.MovieCard
import com.example.iptv_mobile.model.Movie
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.navigation.Screen
import com.example.iptv_mobile.viewmodel.PlaylistViewModel

@Composable
fun MoviesScreen(
    playlist: Playlist,
    playlistViewModel: PlaylistViewModel,
    navController: NavHostController
) {
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var featuredMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var moviesByCategory by remember { mutableStateOf<Map<Long, List<Movie>>>(emptyMap()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val tmdbImageProvider = remember { TMDBImageProvider.getInstance() }

    LaunchedEffect(playlist.id) {
        coroutineScope.launch {
            try {
                categories = playlistViewModel.getCategoriesForPlaylist(playlist.id)
                    .filter { it.isMovie }

                val movieMap = mutableMapOf<Long, List<Movie>>()
                val allMovies = mutableListOf<Movie>()

                categories.forEach { category ->
                    val movies = playlistViewModel.getMoviesForCategory(category.id).sortedByDescending { it.added }
                    movieMap[category.id] = movies
                    allMovies.addAll(movies)
                }

                moviesByCategory = movieMap

                featuredMovies = allMovies.filter { it.isFeatured }.take(5).ifEmpty {
                    allMovies.take(5)
                }

                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                // Handle error
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
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
        } else {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                // --- Featured Movie Section (LazyRow) ---
                if (featuredMovies.isNotEmpty()) {
                    FeaturedSection(
                        movies = featuredMovies,
                        navController = navController,
                        tmdbImageProvider = tmdbImageProvider
                    )
                }

                Spacer(Modifier.height(20.dp))

                categories.forEach { category ->
                    val movies = moviesByCategory[category.id] ?: emptyList()
                    if (movies.isNotEmpty()) {
                        MovieCategoryRow(
                            title = category.name,
                            movies = movies,
                            navController = navController,
                            tmdbImageProvider = tmdbImageProvider
                        )
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedSection(
    movies: List<Movie>,
    navController: NavHostController,
    tmdbImageProvider: TMDBImageProvider
) {
    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    val pagerState = rememberPagerState(pageCount = { movies.size })

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp
        ) { page ->
            Card(
                shape = RoundedCornerShape(16.dp),
                border = if (pagerState.currentPage == page) BorderStroke(2.dp, Color.White) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f) // Make it longer vertically (taller than wide)
                    .clickable {
                        navController.navigate(Screen.MovieDetails.createRoute(movies[page].id))
                    }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        tmdbImageProvider.getBackdropUrl(movies[page].featuredPosterUrl) ?: movies[page].coverUrl
                    ),
                    contentDescription = "Featured Movie",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        PageIndicator(
            numberOfPages = movies.size,
            selectedPage = pagerState.currentPage
        )
    }
}

@Composable
fun MovieCategoryRow(
    title: String,
    movies: List<Movie>,
    navController: NavHostController,
    tmdbImageProvider: TMDBImageProvider
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = "See all",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                MovieCard(
                    movie = movie,
                    tmdbImageProvider = tmdbImageProvider,
                    onClick = {
                        navController.navigate(Screen.MovieDetails.createRoute(movie.id))
                    },
                    modifier = Modifier.width(120.dp),
                    showTitle = true
                )
            }
        }
    }
}

@Composable
fun PageIndicator(numberOfPages: Int, selectedPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until numberOfPages) {
            val width by animateDpAsState(
                targetValue = if (i == selectedPage) 24.dp else 8.dp,
                label = "Indicator Width"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (i == selectedPage) Color.White else Color.Gray)
            )
        }
    }
}

