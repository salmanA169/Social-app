package com.example.social.sa.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class SurfaceColors(
    val surfaceContainerHighest: Color = Color(0xffE3E2E6),
    val surfaceContainerHigh: Color = Color(0xffE3E2E6),
    val surfaceContainer: Color = Color(0xffE3E2E6),
    val surfaceContainerLow: Color = Color(0xffE3E2E6),
    val surfaceContainerLowest: Color = Color(0xffE3E2E6),
    val surfaceBright: Color = Color(0xffE3E2E6),
    val surfaceDim: Color = Color(0xffE3E2E6)
)

object SurfaceColor {

    val surfaces: SurfaceColors
        @Composable
        get() = LocalSurfaceColors.current
}
val LocalSurfaceColors = compositionLocalOf { SurfaceColors() }
val light_surfaceContainerHighest: Color = Color(0xffE3E2E6)
val light_surfaceContainerHigh: Color = Color(0xffE9E8EB)
val light_surfaceContainer: Color = Color(0xffEFEDF1)
val light_surfaceContainerLow: Color = Color(0xffF4F3F7)
val light_surfaceContainerLowest: Color = Color(0xffFFFFFF)
val light_surfaceBright: Color = Color(0xffFAF9FD)
val light_surfaceDim: Color = Color(0xffDBD9DD)

val dark_surfaceContainerHighest: Color = Color(0xff343538)
val dark_surfaceContainerHigh: Color = Color(0xff292A2D)
val dark_surfaceContainer: Color = Color(0xff1F1F23)
val dark_surfaceContainerLow: Color = Color(0xff1A1B1F)
val dark_surfaceContainerLowest: Color = Color(0xff0D0E11)
val dark_surfaceBright: Color = Color(0xff38393C)
val dark_surfaceDim: Color = Color(0xff121316)
val lightSurfaceColors = SurfaceColors(
    light_surfaceContainerHighest,
    light_surfaceContainerHigh,
    light_surfaceContainer,
    light_surfaceContainerLow,
    light_surfaceContainerLowest,
    light_surfaceBright,
    light_surfaceDim
)
val darkSurfaceColors = SurfaceColors(
    dark_surfaceContainerHighest,
    dark_surfaceContainerHigh,
    dark_surfaceContainer,
    dark_surfaceContainerLow,
    dark_surfaceContainerLowest,
    dark_surfaceBright,
    dark_surfaceDim
)