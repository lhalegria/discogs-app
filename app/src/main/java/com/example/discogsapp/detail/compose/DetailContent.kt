package com.example.discogsapp.detail.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.discogsapp.R
import com.example.discogsapp.common.compose.ErrorState
import com.example.discogsapp.common.compose.LoadingState
import com.example.discogsapp.common.compose.TopBar
import com.example.discogsapp.detail.viewmodel.DetailState
import com.example.discogsapp.domain.model.ArtistDetailModel
import com.example.discogsapp.domain.model.ArtistImageModel
import com.example.discogsapp.domain.model.ArtistMemberModel

private enum class DetailContentState {
    Loading,
    Error,
    Success,
}

@Composable
fun DetailContent(
    state: DetailState,
    onClickRelease: (artist: String) -> Unit,
    onRetry: () -> Unit,
    onClickLink: (url: String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val contentState =
        when {
            state.isError() -> DetailContentState.Error
            state.isSuccessful() -> DetailContentState.Success
            else -> DetailContentState.Loading
        }

    AnimatedContent(
        targetState = contentState,
        label = "detail-content-state",
        modifier = Modifier.fillMaxSize(),
    ) { targetState ->
        when (targetState) {
            DetailContentState.Loading -> {
                LoadingState(message = stringResource(R.string.loading_artist_details))
            }

            DetailContentState.Error -> {
                ErrorState(
                    errorMessage = stringResource(R.string.error_loading_artist),
                    isButtonEnabled = !state.isLoading,
                    onRetry = onRetry,
                    onGoBack = onNavigateBack,
                )
            }

            DetailContentState.Success -> {
                state.artistInfo?.let { artist ->
                    DetailSuccessContent(
                        artist = artist,
                        onClickRelease = onClickRelease,
                        onClickLink = onClickLink,
                        onNavigateBack = onNavigateBack,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSuccessContent(
    artist: ArtistDetailModel,
    onClickRelease: (String) -> Unit,
    onClickLink: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = artist.name,
                onNavigateBack = onNavigateBack,
            )
        },
        content = { paddingValues ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
            ) {
                item {
                    artist.images.firstOrNull()?.let { image ->
                        ArtistImage(
                            image = image,
                            artist = artist.name,
                        )
                    }
                }

                if (artist.profile.isNotEmpty()) {
                    item {
                        ArtistProfile(
                            realName = artist.realName,
                            profile = artist.profile,
                        )
                    }
                }

                if (artist.urls.isNotEmpty()) {
                    item {
                        ArtistWebsites(
                            artistUrls = artist.urls,
                            onClickLink = onClickLink,
                        )
                    }
                }

                if (artist.members.isNotEmpty()) {
                    item {
                        ArtistMembers(members = artist.members)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        },
        bottomBar = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(16.dp),
            ) {
                Button(
                    onClick = { onClickRelease(artist.name) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.view_releases_button))
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ArtistImage(
    image: ArtistImageModel,
    artist: String,
) {
    AsyncImage(
        model = image.uri,
        contentDescription = artist,
        contentScale = ContentScale.Crop,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(8.dp)),
    )
}

@Composable
private fun ArtistProfile(
    realName: String,
    profile: String,
) {
    val isAboutExpanded = remember { mutableStateOf(true) }
    ExpandableSection(
        title = stringResource(R.string.about_artist),
        isExpanded = isAboutExpanded.value,
        onToggle = { isAboutExpanded.value = !isAboutExpanded.value },
    ) {
        if (realName.isNotEmpty()) {
            Text(
                text = stringResource(R.string.artist_given_name, realName),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        Text(
            text = profile,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun ArtistWebsites(
    artistUrls: List<String>,
    onClickLink: (String) -> Unit,
) {
    val isSitesExpanded = remember { mutableStateOf(false) }
    ExpandableSection(
        title = stringResource(R.string.artist_websites),
        isExpanded = isSitesExpanded.value,
        onToggle = { isSitesExpanded.value = !isSitesExpanded.value },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            artistUrls.forEach { url ->
                ClickableTextUrl(
                    url = url,
                    onClickLink = onClickLink,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ArtistMembers(
    members: List<ArtistMemberModel>,
) {
    val isMembersExpanded = remember { mutableStateOf(false) }
    ExpandableSection(
        title = stringResource(R.string.artist_members),
        isExpanded = isMembersExpanded.value,
        onToggle = { isMembersExpanded.value = !isMembersExpanded.value },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            members.forEach { member ->
                MemberCard(member = member)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentSuccessPreview() {
    DetailContent(
        state =
            DetailState(
                artistId = 108713,
                artistInfo =
                    ArtistDetailModel(
                        id = 108713,
                        name = "Nickelback",
                        profile =
                            "Nickelback is a Canadian rock band from Hanna, Alberta formed in 1995. " +
                                "Nickelback's music is classed as hard rock and alternative metal.",
                        realName = "Teste",
                        urls =
                            listOf(
                                "http://www.nickelback.com/",
                                "http://en.wikipedia.org/wiki/Nickelback",
                            ),
                        images =
                            listOf(
                                ArtistImageModel(
                                    uri =
                                        "https://api-img.discogs.com/9xJ5T7IBn23DDMpg1USsDJ7IGm4=/330x260/smart/" +
                                            "filters:strip_icc():format(jpeg):mode_rgb():quality(96)/discogs-images/" +
                                            "A-108713-1110576087.jpg.jpg",
                                    smallUri = "",
                                    height = 260,
                                    width = 330,
                                ),
                            ),
                        members =
                            listOf(
                                ArtistMemberModel(
                                    name = "Chad Kroeger",
                                    active = true,
                                ),
                                ArtistMemberModel(
                                    name = "Daniel Adair",
                                    active = true,
                                ),
                                ArtistMemberModel(
                                    name = "Mike Kroeger",
                                    active = true,
                                ),
                            ),
                    ),
            ),
        onClickRelease = {},
        onRetry = {},
        onClickLink = {},
        onNavigateBack = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailContentLoadingPreview() {
    DetailContent(
        state = DetailState(),
        onClickRelease = {},
        onRetry = {},
        onClickLink = {},
        onNavigateBack = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailContentErrorPreview() {
    DetailContent(
        state =
            DetailState(
                artistId = 108713,
                artistInfo = null,
                hasError = true,
            ),
        onClickRelease = {},
        onRetry = {},
        onClickLink = {},
        onNavigateBack = {},
    )
}
