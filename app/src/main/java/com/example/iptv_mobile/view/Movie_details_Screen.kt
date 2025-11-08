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
import com.example.iptv_mobile.model.Movie
import com.example.iptv_mobile.viewmodel.PlaylistViewModel
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsScreen(
    movieId: Long,
    playlistViewModel: PlaylistViewModel,
    onBack: () -> Unit = {}
) {
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        coroutineScope.launch {
            movie = playlistViewModel.getMovieById(movieId)
            isLoading = false
        }
    }

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
        } else if (movie != null) {
            val currentMovie = movie!!

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
                        Image(
                            painter = rememberAsyncImagePainter(currentMovie.coverUrl),
                            contentDescription = currentMovie.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent),
                                        startY = 800f,
                                        endY = 0f
                                    )
                                )
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
