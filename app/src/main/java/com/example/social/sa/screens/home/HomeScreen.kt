package com.example.social.sa.screens.home

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.ExpandText
import com.example.social.sa.component.RoundedFilterChip
import com.example.social.sa.component.defaultRoundedFilterChipColors
import com.example.social.sa.component.nestedScrollConnectionNoAction
import com.example.social.sa.component.selectedRoundedFilterChipLike
import com.example.social.sa.model.Comment
import com.example.social.sa.model.Posts
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.format
import com.google.android.play.integrity.internal.w
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.random.Random

fun NavGraphBuilder.homeDest(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.HomeScreen.route) {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        val state by homeViewModel.state.collectAsState()
        HomeScreen(state = state, paddingValues)

    }
}

@Composable
fun Comments(modifier: Modifier = Modifier, comments: List<Comment>) {
    LazyColumn {
        items(comments){
            CommentItem(
                imageUri = it.imageUrl,
                displayName = it.displayName,
                time = "2d",
                comment = it.comment,
                likes = it.likes
            )
        }
    }
}

@Composable
fun CommentItem(
    modifier: Modifier = Modifier,
    imageUri: String,
    displayName: String,
    time: String,
    comment: String,
    likes: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = "https://t3.ftcdn.net/jpg/05/35/47/38/360_F_535473874_OWCa2ohzXXNZgqnlzF9QETsnbrSO9pFS.jpg",
            contentDescription = "sender image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.text_image)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier) {
            Row {
                Text(text = "salman", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "2d",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(text = comment, fontWeight = FontWeight.Medium)
            Text(
                text = "Replay",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Light,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.black_like_icon),
                    contentDescription = "liked"
                )
                Text(text = likes.toString())
            }
        }
    }
}
val COMMENTS = (0..50).map {
    Comment(
        "","",
        "",
        "https://t3.ftcdn.net/jpg/05/35/47/38/360_F_535473874_OWCa2ohzXXNZgqnlzF9QETsnbrSO9pFS.jpg",
        "salman","test","2d",15
    )
}
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    paddingValues: PaddingValues
) {

    var showComments by remember{
        mutableStateOf(false)
    }
    val testComment = remember{
        COMMENTS
    }
    if (showComments){
        ModalBottomSheet(onDismissRequest = { showComments = false }) {
            Comments(comments = testComment)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(4.dp)
    ) {
        if (state.isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        val tabs = remember {
            TabItem.values().toList()
        }
        val pagerState = rememberPagerState {
            tabs.size
        }
        var currentTabs by remember {
            mutableStateOf(TabItem.HOME)
        }
        val scope = rememberCoroutineScope()
        FilterHomePosts(currentTabItem = currentTabs, tabs = tabs) {
            scope.launch {
                currentTabs = TabItem.values()[it]
                pagerState.animateScrollToPage(it)
            }
        }
        HorizontalDivider()

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize(),
            pageNestedScrollConnection = nestedScrollConnectionNoAction
        ) {
            when (tabs[it]) {
                TabItem.HOME -> {
                    Posts(state.homePosts, onCommentClick = {
                        showComments = true
                    })
                }

                TabItem.FOR_YOU -> {
//                    Posts(state.forYouPosts)
                }
            }
        }
    }
}

@Composable
fun FilterHomePosts(
    modifier: Modifier = Modifier,
    currentTabItem: TabItem,
    tabs: List<TabItem>,
    onClick: (Int) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach {
            RoundedFilterChip(
                selected = it == currentTabItem,
                onClick = {
                    onClick(tabs.indexOf(it))
                },
                label = stringResource(id = it.tabName),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
fun Posts(
    posts: List<Posts>,
    onCommentClick:()->Unit
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(posts) {
            Post(
                post = it,
                onCommentClick = onCommentClick
            )
        }
    }
}

@Composable
fun Post(
    modifier: Modifier = Modifier,
    post: Posts,
    onCommentClick: () -> Unit
) {
    var showMoreContent by rememberSaveable {
        mutableStateOf(false)
    }
    Column() {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://t3.ftcdn.net/jpg/05/35/47/38/360_F_535473874_OWCa2ohzXXNZgqnlzF9QETsnbrSO9pFS.jpg",
                contentDescription = "sender image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.text_image)
            )
            Spacer(modifier = Modifier.width(9.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "salman", fontWeight = FontWeight.Medium)
                Row {
                    Text(text = "@salman179", fontWeight = FontWeight.Normal)
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurface,
                                CircleShape
                            )
                            .clip(
                                CircleShape
                            )
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 2.dp)
                    )
                    Text(
                        text = "2d",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))


            }


            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.dots_icon),
                    contentDescription = "dots icon"
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 36.dp)
                .animateContentSize()
        ) {
            ExpandText(
                text = post.content, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface
                ), showMore = showMoreContent
            ) {
                showMoreContent = it
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                post.images.forEach {
                    AsyncImage(
                        model = it,
                        contentDescription = "Image Content",
                        modifier = Modifier
                            .size(230.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

            }

            PostInfo(
                likeCount = post.likes,
                commentCount = post.comments,
                shareCount = post.share,
                isLiked = post.likes != 0,
                onCommentClick = onCommentClick
            )

        }
        HorizontalDivider()
    }
}

@Composable
fun PostInfo(
    modifier: Modifier = Modifier,
    likeCount: Int,
    commentCount: Int,
    shareCount: Int,
    isLiked: Boolean,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedFilterChip(
            selected = isLiked,
            onClick = { /*TODO*/ },
            label = likeCount.toString(),
            leadingIcon = painterResource(
                id = if (!isLiked) R.drawable.black_like_icon else R.drawable.liked_icon
            ),
            colors = if (!isLiked) defaultRoundedFilterChipColors() else selectedRoundedFilterChipLike()
        )
        RoundedFilterChip(
            selected = false,
            onClick = onCommentClick,
            label = likeCount.toString(),
            leadingIcon = painterResource(
                id = R.drawable.comments_icon
            ),
            colors = defaultRoundedFilterChipColors()
        )
        RoundedFilterChip(
            selected = false,
            onClick = { /*TODO*/ },
            label = likeCount.toString(),
            leadingIcon = painterResource(
                id = R.drawable.share_icon
            ),
            colors = defaultRoundedFilterChipColors()
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedIconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(34.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.bookmark_icon),
                contentDescription = "book mark"
            )
        }
    }

}

@Preview(
    showBackground = true, showSystemUi = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun PostsPreview() {
    SocialTheme {
        CommentItem(
            imageUri = "",
            displayName = "salman",
            time = "aaaaa",
            comment = "sss",
            likes = 5
        )
    }
}