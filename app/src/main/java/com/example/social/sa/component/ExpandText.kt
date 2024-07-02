package com.example.social.sa.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlin.math.log

@Composable
fun ExpandText(
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    text: String,
    maxLines: Int = 4,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(LocalContentColor.current),
    onExpandClick: (Boolean) -> Unit
) {
    var textLayout by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    var finalText by remember {
        mutableStateOf(text)
    }
    var annotatedString by remember {
        mutableStateOf<AnnotatedString?>(buildAnnotatedString { append(text) })
    }

    val color = MaterialTheme.colorScheme.primary
    LaunchedEffect(key1 = textLayout) {
        textLayout?.let { textLayout ->
            when {
                showMore -> {
                    annotatedString = buildAnnotatedString {
                        append(text)
                        withStyle(
                            SpanStyle(
                                color = color
                            )
                        ) {
                            pushStringAnnotation(tag = "show less", annotation = "show less")
                            append("..show less")
                        }
                    }
                }

                !showMore -> {
                    try {
                        val lastChar = textLayout.getLineEnd(maxLines - 1)
                        val adjustedString = text
                            .substring(0, lastChar)
                            .dropLast("..show more".length)
                            .dropLastWhile { it == ' ' || it == '.' }
                        annotatedString = buildAnnotatedString {
                            append(adjustedString)
                            withStyle(
                                SpanStyle(
                                    color = color
                                )
                            ) {
                                pushStringAnnotation(tag = "show more", annotation = "show more")
                                append("..show more")
                            }
                        }
                    }catch (e:Exception){
                        annotatedString = buildAnnotatedString { append(text) }
                    }

                }
            }
        }
    }
    ClickableText(
        text = annotatedString ?: buildAnnotatedString { },
        maxLines = if (showMore) Int.MAX_VALUE else maxLines,
        onTextLayout = { textLayout = it },
        modifier = modifier,
        style = style

    ) { offset ->
        annotatedString?.let {
            it.getStringAnnotations(offset, offset).firstOrNull()?.let {
                onExpandClick(!showMore)
            }
        }
    }

}