package com.example.social.sa.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.social.sa.Screens

fun NavGraphBuilder.searchDest(navController: NavController) {
    composable<Screens.SearchRoute> {
        val searchViewModel = hiltViewModel<SearchViewModel>()
        val effect by searchViewModel.effect.collectAsStateWithLifecycle()
        val state by searchViewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = effect) {
            when (effect) {
                SearchEffect.PopBack -> {
                    navController.popBackStack()
                }

                null -> {}
                is SearchEffect.NavigateUserProfile -> {
                    navController.navigate(Screens.UserInfoRoute((effect as SearchEffect.NavigateUserProfile).userUUID))
                }
            }
            searchViewModel.resetEffect()
        }
        SearchScreen(searchState = state, searchViewModel::onEvent)
    }
}

@Composable
fun SearchScreen(
    searchState: SearchState,
    onEvent: (SearchEvent) -> Unit = {}
) {

    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

        TextField(
            value = searchState.query,
            onValueChange = { onEvent(SearchEvent.Query(it)) },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchState.users, key = {
                it.userUUID
            }) {
                ListItem(
                    modifier = Modifier
                        .clickable { onEvent(SearchEvent.NavigateUserProfile(it.userUUID)) }
                        .animateItem(),
                    headlineContent = { Text(it.displayName) },
                    colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainerHighest),
                    leadingContent = {
                        AsyncImage(
                            model = it.imageUri, contentDescription = "", modifier = Modifier
                                .size(48.dp)
                                .clip(
                                    CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )
                    },
                    supportingContent = {
                        Text(text = it.userId)
                    }
                )
            }
        }
    }
}