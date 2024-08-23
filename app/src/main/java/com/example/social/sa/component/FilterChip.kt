package com.example.social.sa.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.social.sa.ui.theme.likeColor
import com.example.social.sa.ui.theme.likeSurfaceColor

@Composable
fun defaultRoundedFilterChipColors()  = FilterChipDefaults.filterChipColors(
    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
)
@Composable
fun selectedRoundedFilterChipLike()= FilterChipDefaults.filterChipColors(
    selectedLabelColor = likeColor,
    selectedLeadingIconColor = likeColor,
    selectedContainerColor = likeSurfaceColor
)
@Composable
fun RoundedFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    leadingIcon: Painter?= null,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors()
) {
    FilterChip(
        shape = CircleShape,
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                modifier = Modifier.padding(4.dp),
            )
        },
        colors = colors,
        leadingIcon = {
            leadingIcon?.let {
                Icon(painter =it , contentDescription = "")
            }
        }
    )
}