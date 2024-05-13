package com.example.social.sa

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.social.sa.component.HomeAppBar
import com.example.social.sa.screens.home.homeDest
import com.example.social.sa.screens.home.add_edit_post.addEditPostDest
import com.example.social.sa.screens.register.registerDest
import com.example.social.sa.ui.theme.SocialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            }
        requestPermission.launch(
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        )
        setContent {
            SocialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val controller = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val state by mainViewModel.state.collectAsStateWithLifecycle()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    LaunchedEffect(key1 = state.shouldNavigateLoginScreen) {
        if (state.shouldNavigateLoginScreen) {
            controller.navigate(Screens.RegisterScreen.route) {
                popUpTo(Screens.HomeScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    val screens = remember {
        bottomScreens
    }

    val shouldShow = remember(state.shouldNavigateLoginScreen, currentDestination?.route ?: "") {
        !state.shouldNavigateLoginScreen && screens.any { it.route == currentDestination?.route }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        // TODO: improve it later
        AnimatedVisibility(visible = shouldShow) {
            NavigationBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            controller.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(controller.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }

                        },
                        icon = {
                            Icon(painter = screen.icon.getIcon(), contentDescription = "")
                        })
                }
            }
        }
    }, topBar = {
        AnimatedVisibility(visible = shouldShow) {
            HomeAppBar(image = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png")
        }
    }, floatingActionButton = {
        AnimatedVisibility(visible = shouldShow) {
            FloatingActionButton(onClick = {
                controller.navigate(Screens.PostReviewScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    }) {

        NavHost(
            route = "ss",
            navController = controller,
            startDestination = Screens.HomeScreen.route
        ) {
            homeDest(controller, it)
            addEditPostDest(controller)
            composable(Screens.SearchScreen.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Search", Modifier.align(Alignment.Center))
                }
            }
            composable(Screens.NotificationScreen.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "notificatoin", Modifier.align(Alignment.Center))
                }
            }
            composable(Screens.InboxScreen.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Inbox", Modifier.align(Alignment.Center))
                }
            }
            registerDest(controller)
        }
    }
}


