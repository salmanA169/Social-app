package com.example.social.sa.screens.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.social.sa.MainActivity
import com.example.social.sa.Screens

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.discoverDest(navController: NavController,paddingValues: PaddingValues){
    composable(Screens.DiscoverScreen.route) {
        val searchViewModel = hiltViewModel<DiscoverViewModel>(LocalContext.current as MainActivity)
        var isSearch by remember { mutableStateOf(false) }
        Column(modifier = Modifier.padding(paddingValues)) {
//                SearchBar(
//                    inputField = {
//                        SearchBarDefaults.InputField(
//                            query = "",
//                            onQueryChange = {},
//                            onSearch = {},
//                            expanded = isSearch,
//                            onExpandedChange = { isSearch = it },
//
//                            )
//                    },
//                    expanded = isSearch,
//                    onExpandedChange = {
//                        isSearch = it
//                    },
//                    modifier = Modifier,
//                    content = {
//                        LazyColumn {
//                            items(30){
//                                Text("sssssssssssssssss")
//                            }
//                        }
//                    },
//                )
            Text(text = "test search")
        }

    }
}