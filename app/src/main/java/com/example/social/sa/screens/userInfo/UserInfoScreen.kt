package com.example.social.sa.screens.userInfo

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        UserInfoScreen(userInfoState = UserInfoState(
            postsUser = listOf(
                Posts(
                    "",
                    "",
                    "Salman Dev",
                    "",
                    LocalDateTime.now(),
                    "Testssssss",
                    listOf("https://firebasestorage.googleapis.com/v0/b/social-43bfb.appspot.com/o/GstBVqvQLNelKYLdnkHOr9xCwV73%2Fimage_profile%2F1000000035.webp?alt=media&token=b4a7b234-5947-426b-a227-5f9a2ae79ba1"),
                    10,
                    10,
                    10,
                ),
                Posts(
                    "",
                    "",
                    "Salman Dev",
                    "",
                    LocalDateTime.now(),
                    "Testssssss",
                    listOf("https://firebasestorage.googleapis.com/v0/b/social-43bfb.appspot.com/o/B7gOVQc1IbZYfsPJ9IBKCZ65oOG2%2Fimage_profile%2F1000158970.webp?alt=media&token=e6352309-d643-4716-b334-de9e83151fea"),
                    10,
                    10,
                    10,
                )
            ),
            userInfo = UserInfo(
                "",
                "",
                "salman Dev",
                "https://t3.ftcdn.net/jpg/05/35/47/38/360_F_535473874_OWCa2ohzXXNZgqnlzF9QETsnbrSO9pFS.jpg",
                Timestamp.now(),
                "",
                120,
                456,
                123,
                ""
            )
        )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(modifier: Modifier = Modifier, userInfoState: UserInfoState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = userInfoState.userInfo.displayName,
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
                UserImageAndFollowing(
                    imageUri = userInfoState.userInfo.imageUri,
                    following = userInfoState.userInfo.following.toString(),
                    followers = userInfoState.userInfo.followers.toString(),
                    postCount = userInfoState.userInfo.postsCount.toString()
                )
            }
            item {
                Text(
                    text = userInfoState.userInfo.bio, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                ButtonActions(isFollowing = true)
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
fun ButtonActions(modifier: Modifier = Modifier, isFollowing: Boolean) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = if (isFollowing) "Follow" else "Following"
        ) {

        }
        SurfaceButton(text = "Message", modifier = Modifier.weight(1.5f)) {

        }
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
            placeholder = painterResource(id = R.drawable.text_image)
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
                        "Salman Dev",
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
                    ""
                )
            )
        )
    }
}