package com.atech.ui_common.common

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.atech.ui_common.R

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    url: String? = null,
    onClick: () -> Unit = {},
) {
    AnimatedVisibility(visible = url != null) {
        IconButton(onClick = onClick) {
            ImageLoader(
                modifier = Modifier
                    .size(30.dp),
                imageUrl = url,
                isRounderCorner = true
            )
        }
    }
    AnimatedVisibility(visible = url == null) {
        ImageIconButton(
            modifier = modifier,
            icon = Icons.Default.AccountCircle,
            tint = MaterialTheme.colorScheme.primary,
            onClick = onClick,
            contextDes = R.string.profile,
        )
    }
}

@Composable
fun ImageIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color = LocalContentColor.current,
    @StringRes contextDes: Int? = null,
    onClick: () -> Unit = {},
    isEnable: Boolean = true
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() },
        enabled = isEnable
    ) {
        Icon(
            imageVector = icon,
            contentDescription =
            if (contextDes == null) null else stringResource(id = contextDes),
            tint = tint
        )
    }
}