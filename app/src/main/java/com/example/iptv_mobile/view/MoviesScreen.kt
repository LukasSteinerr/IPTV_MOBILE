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

@Composable
fun MoviesScreen() {
    val tabs = listOf("TV Series", "Movies", "Animes", "Kids", "News")
    var selectedTab by remember { mutableStateOf("Movies") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // --- Top Bar ---
        TopBar()

        Spacer(Modifier.height(12.dp))

        // --- Tabs ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEach { tab ->
                Text(
                    text = tab,
                    color = if (tab == selectedTab) Color.White else Color.Gray,
                    fontWeight = if (tab == selectedTab) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable { selectedTab = tab }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- Featured Movie Section (LazyRow) ---
        FeaturedSection()

        Spacer(Modifier.height(20.dp))

        // --- Continue Watch Section ---
        ContinueWatchingSection()

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile picture placeholder (using Coil)
        Image(
            painter = rememberAsyncImagePainter("https://picsum.photos/40/40"),
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedSection() {
    val featuredUrls = listOf(
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/kdkk7OBnIL1peW2zwcAAp6O54Jo.jpg",
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/slKAbvY2CjAIyJFqoLSh1WICzg6.jpg",
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/pHpq9yNUIo6aDoCXEBzjSolywgz.jpg"
    )

    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)

    val pagerState = rememberPagerState(pageCount = { featuredUrls.size })

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
            ) {
                Image(
                    painter = rememberAsyncImagePainter(featuredUrls[page]),
                    contentDescription = "Featured Movie",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        PageIndicator(
            numberOfPages = featuredUrls.size,
            selectedPage = pagerState.currentPage
        )
    }
}

@Composable
fun ContinueWatchingSection() {
    // Using placeholder URLs for images
    val movieUrls = listOf(
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/5Gr4amaB1xxeYAEMOdrVutaWwgz.jpg",
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/q8dWfc4JwQuv3HayIZeO84jAXED.jpg",
        "https://image.tmdb.org/t/p/w600_and_h900_bestv2/ecflk7AZf0ij205yDswjlvdxlCO.jpg"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Continue Watch", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("See all", color = Color.Gray, fontSize = 14.sp)
    }

    Spacer(Modifier.height(12.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movieUrls) { url ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(120.dp)
                    .height(160.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Play icon overlay
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Play",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(48.dp)
                    )
                }
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

