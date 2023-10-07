package com.example.social.sa.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
annotation class PreviewBothLightAndDark