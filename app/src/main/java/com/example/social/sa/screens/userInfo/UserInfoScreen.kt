package com.example.social.sa.screens.userInfo

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.PrimaryButton
import com.example.social.sa.component.SurfaceButton
import com.example.social.sa.model.Posts
import com.example.social.sa.model.UserInfo
import com.example.social.sa.screens.home.Post
import com.example.social.sa.ui.theme.SocialTheme
import com.google.firebase.Timestamp
import java.time.LocalDateTime

fun NavGraphBuilder.userInfoDest(navController: NavController) {
    composable<Screens.UserInfoRoute> {
        val getUserUUid = it.toRoute<Screens.UserInfoRoute>()
        val infoViewModel = hiltViewModel<UserInfoViewModel>()
        val state by infoViewModel.state.collectAsStateWithLifecycle()
        val effect by infoViewModel.effect.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = true) {
            infoViewModel.getUserInfo(getUserUUid.userUid)
        }
        LaunchedEffect(key1 = effect) {
            when (effect) {
                is UserInfoEffect.NavigateToMessageScreen -> {
                    navController.navigate(Screens.MessageRoute((effect as UserInfoEffect.NavigateToMessageScreen).chatId))
                }

                null -> Unit
            }
            infoViewModel.resetEffect()
        }
        UserInfoScreen(
            userInfoState = state,
            onEvent = infoViewModel::onEvent
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    userInfoState: UserInfoState,
    onEvent: (UserInfoEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = userInfoState.userInfo?.displayName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            }, navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = "Back"
                    )
                }
            })
        }
    ) {
        LazyColumn(modifier = modifier.padding(it)) {

            item {
                if (userInfoState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                UserImageAndFollowing(
                    imageUri = userInfoState.userInfo?.imageUri ?: "",
                    following = userInfoState.userInfo?.following?.toString() ?: "0",
                    followers = userInfoState.userInfo?.followers?.toString() ?: "0",
                    postCount = userInfoState.userInfo?.postsCount?.toString() ?: "0"
                )
            }
            item {
                Text(
                    text = userInfoState.userInfo?.bio ?: "", modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                ButtonActions(
                    isFollowing = userInfoState.isFollowing,
                    onMessageClick = { onEvent(UserInfoEvent.MessageClick) },
                    onFollowClick = {
                        onEvent(UserInfoEvent.RequestFollow)
                    },
                    onUnFollowClick = {
                        onEvent(UserInfoEvent.UnFollowEvent)
                    }
                    )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
            }
            items(userInfoState.postsUser) {
                Post(post = it, onUserClick = {}, onCommentClick = {}, onPreviewImageNavigate = {})
            }
        }
    }
}

@Composable
fun ButtonActions(
    modifier: Modifier = Modifier,
    isFollowing: Boolean,
    onMessageClick: () -> Unit,
    onFollowClick: () -> Unit,
    onUnFollowClick:()-> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = if (isFollowing) "Following" else "Follow",
            onClick = if (isFollowing) onUnFollowClick else onFollowClick,
            colors = if (isFollowing) ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) else ButtonDefaults.buttonColors()
        )
        SurfaceButton(text = "Message", modifier = Modifier.weight(1.5f), onClick = onMessageClick)
    }
}

@Composable
fun UserImageAndFollowing(
    modifier: Modifier = Modifier,
    imageUri: String,
    following: String,
    followers: String,
    postCount: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = "image user",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        LabelAndCount(label = stringResource(id = R.string.postsCount), count = postCount)
        LabelAndCount(label = stringResource(id = R.string.followers), count = followers)
        LabelAndCount(label = stringResource(id = R.string.following), count = following)
    }
}

@Composable
fun LabelAndCount(modifier: Modifier = Modifier, label: String, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = count)
    }
}

@Preview
@Composable
private fun UserInfoPreview() {
    SocialTheme {
        UserInfoScreen(
            userInfoState = UserInfoState(
                postsUser = listOf(
                    Posts(
                        "",
                        "",
                        "Salman Dev", "",
                        "",
                        LocalDateTime.now(),
                        "Testssssss",
                        listOf(),
                        10,
                        10,
                        10,
                    )
                ),
                userInfo = UserInfo(
                    "",
                    "",
                    "salman Dev",
                    "",
                    Timestamp.now(),
                    "",
                    120,
                    456,
                    123,
                    "", listOf()
                )
            )
        )
    }
}