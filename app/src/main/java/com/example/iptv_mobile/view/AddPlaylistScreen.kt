package com.example.iptv_mobile.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iptv_mobile.model.Playlist
import com.example.iptv_mobile.model.PlaylistTypeConstants
import com.example.iptv_mobile.repository.PlaylistService
import com.example.iptv_mobile.viewmodel.PlaylistViewModel
import com.example.iptv_mobile.viewmodel.PlaylistViewModelFactory

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddPlaylistScreen(
    playlistService: PlaylistService,
    onPlaylistAdded: () -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModelFactory(playlistService))
) {
    var currentStep by remember { mutableStateOf(1) }
    var playlistType by remember { mutableStateOf(PlaylistTypeConstants.xtream) }
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by playlistViewModel.isLoading
    val loadingMessage by playlistViewModel.loadingMessage
    val errorMessage by playlistViewModel.errorMessage

    val initialFocusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Playlist") }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = currentStep,
            modifier = Modifier.padding(paddingValues),
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                } else {
                    slideInHorizontally(initialOffsetX = { -it }) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                }
            }
        ) { step ->
            when (step) {
                1 -> Step1_ChoosePlaylistType(
                    onTypeSelected = { type ->
                        playlistType = type
                        currentStep = 2
                    },
                    onCancel = onPlaylistAdded,
                    onClearDatabase = { playlistViewModel.clearDatabase() },
                    focusRequester = initialFocusRequester
                )
                2 -> Step2_NamePlaylist(
                    playlistName = name,
                    onPlaylistNameChange = { name = it },
                    onNext = { currentStep = 3 },
                    onBack = { currentStep = 1 },
                    focusRequester = initialFocusRequester
                )
                3 -> Step3_EnterDetails(
                    playlistType = playlistType,
                    url = url,
                    onUrlChange = { url = it },
                    username = username,
                    onUsernameChange = { username = it },
                    password = password,
                    onPasswordChange = { password = it },
                    onNext = { currentStep = 4 },
                    onBack = { currentStep = 2 },
                    focusRequester = initialFocusRequester
                )
                4 -> Step4_Processing(
                    isLoading = isLoading,
                    loadingMessage = loadingMessage,
                    errorMessage = errorMessage,
                    onAddPlaylist = {
                        val newPlaylist = Playlist(
                            name = name,
                            url = url,
                            username = if (playlistType == PlaylistTypeConstants.xtream) username else null,
                            password = if (playlistType == PlaylistTypeConstants.xtream) password else null,
                            typeInt = playlistType
                        )
                        playlistViewModel.addPlaylist(newPlaylist)
                    },
                    onDone = onPlaylistAdded,
                    onBack = { currentStep = 3 },
                    focusRequester = initialFocusRequester
                )
            }
        }
    }
}

@Composable
fun Step1_ChoosePlaylistType(
    onTypeSelected: (Int) -> Unit,
    onCancel: () -> Unit,
    onClearDatabase: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onTypeSelected(PlaylistTypeConstants.m3u) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        ) {
            Text("M3U Playlist")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onTypeSelected(PlaylistTypeConstants.xtream) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xtream Codes")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClearDatabase,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Database")
        }
    }
}

@Composable
fun Step2_NamePlaylist(
    playlistName: String,
    onPlaylistNameChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = playlistName,
            onValueChange = onPlaylistNameChange,
            label = { Text("Playlist Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext, enabled = playlistName.isNotBlank()) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step3_EnterDetails(
    playlistType: Int,
    url: String,
    onUrlChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            label = { Text(if (playlistType == PlaylistTypeConstants.xtream) "Server Address" else "Playlist URL") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        if (playlistType == PlaylistTypeConstants.xtream) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onNext, enabled = url.isNotBlank()) {
                Text("Next")
            }
        }
    }
}

@Composable
fun Step4_Processing(
    isLoading: Boolean,
    loadingMessage: String,
    errorMessage: String,
    onAddPlaylist: () -> Unit,
    onDone: () -> Unit,
    onBack: () -> Unit,
    focusRequester: FocusRequester
) {
    LaunchedEffect(Unit) {
        onAddPlaylist()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(loadingMessage)
        } else if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        } else {
            Text("Playlist added successfully!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDone,
                modifier = Modifier.focusRequester(focusRequester)
            ) {
                Text("Done")
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}