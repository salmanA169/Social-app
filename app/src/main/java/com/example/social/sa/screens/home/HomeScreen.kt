package com.example.social.sa.screens.home

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.social.sa.R
import com.example.social.sa.Screens
import com.example.social.sa.component.ExpandText
import com.example.social.sa.model.Posts
import com.example.social.sa.ui.theme.SocialTheme
import com.example.social.sa.utils.PreviewBothLightAndDark
import kotlinx.coroutines.launch

fun NavGraphBuilder.homeDest(navController: NavController, paddingValues: PaddingValues) {
    composable(Screens.HomeScreen.route) {
        val homeViewModel = hiltViewModel<HomeViewModel>()
        val state by homeViewModel.state.collectAsState()
        HomeScreen(state = state, paddingValues)

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
        var currentTabs by remember{
            mutableStateOf(TabItem.HOME)
        }
        val scope = rememberCoroutineScope()
        FilterHomePosts(currentTabItem = currentTabs, tabs =tabs ) {
            scope.launch {
                Log.d("Home screen compose", "HomeScreen: called index $it")
                currentTabs = TabItem.values()[it]
                pagerState.animateScrollToPage(it)
            }
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
        ) {
            when (tabs[it]) {
                TabItem.HOME -> {
//                    Posts(state.homePosts)
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
    onClick:(Int)->Unit
) {
    // TODO: continue here
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach {
            FilterChip(shape = CircleShape,selected = currentTabItem == it, onClick = {
            onClick(it.ordinal)
            }, label = { Text(text = stringResource(id = it.tabName),modifier = Modifier.padding(6.dp)) })
        }
    }
}

//@Composable
//fun Posts(
//    posts: List<Posts>
//) {
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(posts) {
//            Post(
//                postId = it.postId,
//                uidUser = it.uidUser,
//                profileImage = it.profileUser,
//                userName = it.userName,
//                displayName = it.userName,
//                dateTime = it.dateTime,
//                imageContent = it.imageContent,
//                contentText = it.contentText,
//                comments = it.comments.size,
//                likes = it.likes.size
//            )
//        }
//    }
//}

@Composable
fun Post(
    modifier: Modifier = Modifier,
    postId: String,
    uidUser: String,
    profileImage: String,
    userName: String,
    displayName: String,
    dateTime: String,
    imageContent: String?,
    contentText: String,
    comments: Int,
    likes: Int
) {
    var showMoreContent by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row {
                    Text(text = userName, style = MaterialTheme.typography.labelSmall)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(4.dp)
                            .background(
                                MaterialTheme.colorScheme.onBackground,
                                CircleShape
                            )
                            .align(CenterVertically)
                    )
                    Text(text = dateTime, style = MaterialTheme.typography.labelSmall)


                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
            }
        }
        imageContent?.let {
            AsyncImage(
                model = it,
                contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .heightIn(250.dp, 500.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(30f)),
                contentScale = ContentScale.Crop
            )
        }

        ExpandText(
            text = contentText,
            onExpandClick = { showMoreContent = it },
            showMore = showMoreContent,
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize()
        )
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconWithText(
                Modifier.weight(1f),
                icon = painterResource(id = R.drawable.unlike_icon),
                label = "500"
            )
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            IconWithText(
                Modifier.weight(1f),
                icon = painterResource(id = R.drawable.comment_icon),
                label = "500"
            )
        }
        Divider()
    }
}

@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    icon: Painter,
    label: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(painter = icon, contentDescription = "")
        Spacer(modifier = Modifier.width(14.dp))
        Text(text = label, fontWeight = FontWeight.Light)
    }
}

@PreviewBothLightAndDark
@Composable
fun PostPreview() {
    SocialTheme {
        LazyColumn(content = {
            items(50) {
                Post(
                    postId = "0",
                    uidUser = "",
                    profileImage = "",
                    userName = "@salman",
                    displayName = "salman",
                    dateTime = "3d",
                    imageContent = "",
                    contentText = "1222222222222",
                    comments = 150,
                    likes = 150
                )
            }
        })

    }
}