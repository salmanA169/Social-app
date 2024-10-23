package com.example.social.sa

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NamedNavArgument
import kotlinx.serialization.Serializable

sealed class Screens(val route: String) {


    abstract val args: List<NamedNavArgument>

    @Serializable
    data class UserInfoRoute(val userUid:String)

    @Serializable
    data class MediaPreviewScreen(val mediaType: String,val uri:String)

    @Serializable
    data class InfoRegisterRoute(val email:String,val userName:String?,val imageUrl:String?,val isGoogleProvider :Boolean )

    @Serializable
    data class MessageRoute(val chatId:String)

    @Serializable
    data object SearchRoute

    //    data object MediaPreviewScreen:Screens("media-preview-route"){
//        override val args: List<NamedNavArgument>
//            get() = listOf( navArgument(ConstantsArg.MEDIA_PREVIEW_SCREEN_MEDIA_TYPE_ARG){
//                type = NavArgs
//            })
//
//    }

    data object RegisterScreen:Screens("register-route") {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    @Serializable
    object PostReviewScreen


    sealed class BottomScreens(route: String, val icon: BottomIcon, val label: String = "") :
        Screens(route)

    object HomeScreen : BottomScreens("Home-route", drawableBottomIcon(R.drawable.dashboard_icon)) {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object DiscoverScreen :
        BottomScreens("Discover-route", drawableBottomIcon(R.drawable.search_icon)) {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object NotificationScreen : BottomScreens(
        "Notification-route",
        drawableBottomIcon(R.drawable.clarity_notification_icon)
    ) {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    object InboxScreen : BottomScreens("Inbox-route", drawableBottomIcon(R.drawable.inbox_icon)) {
        override val args: List<NamedNavArgument>
            get() = emptyList()
    }

    @Serializable
    data object CameraPreviewScreen
}

val bottomScreens = listOf<Screens.BottomScreens>(
    Screens.HomeScreen,
    Screens.DiscoverScreen,
    Screens.NotificationScreen,
    Screens.InboxScreen
)


sealed interface BottomIcon {
    class VectorBottomIcon(val image: ImageVector) : BottomIcon
    class DrawableBottomIcon(@DrawableRes val drawable: Int) : BottomIcon

    @Composable
    fun getIcon(): Painter {
        return when (this) {
            is VectorBottomIcon -> rememberVectorPainter(image = image)
            is DrawableBottomIcon -> painterResource(id = drawable)
        }
    }
}

fun vectorBottomIcon(image: ImageVector) = BottomIcon.VectorBottomIcon(image)
fun drawableBottomIcon(@DrawableRes drawableRes: Int) = BottomIcon.DrawableBottomIcon(drawableRes)