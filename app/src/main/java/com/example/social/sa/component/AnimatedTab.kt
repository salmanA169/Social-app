package com.example.social.sa.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.social.sa.ui.theme.SurfaceColor


@Composable
fun AnimatedTab(
    items: List<String>,
    modifier: Modifier = Modifier,
    indicatorPadding: Dp = 4.dp,
    selectedItemIndex: Int = 0,
    onSelectedItemIndex: (Int) -> Unit
) {
    var tabWidth by remember {
        mutableStateOf(0.dp)
    }

    val indicatorOffset: Dp by animateDpAsState(
        targetValue =
        if (selectedItemIndex == 0) {
            tabWidth * (selectedItemIndex / items.size.toFloat())
        } else {
            tabWidth * (selectedItemIndex / items.size.toFloat()) - indicatorPadding
        }, label = ""
    )

    Box(modifier = modifier
        .onGloballyPositioned {
            tabWidth = it.size.width.dp
        }
        .background(SurfaceColor.surfaces.surfaceContainerHigh, CircleShape)) {
        MyTabIndicator()
    }
}

@Composable
fun MyTabIndicator(
    modifier:Modifier = Modifier,

) {
    
}