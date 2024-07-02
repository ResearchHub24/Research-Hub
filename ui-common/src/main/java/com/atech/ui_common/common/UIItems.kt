package com.atech.ui_common.common

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@Composable
fun TitleComposable(
    modifier: Modifier = Modifier,
    title: String,
    padding: Dp = MaterialTheme.spacing.medium
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.captionColor
    )
}

@Composable
fun TextItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    endIcon: ImageVector? = null,
    onEndIconClick: (() -> Unit)? = null
) {
    Surface(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier
                    .padding(MaterialTheme.spacing.medium),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (endIcon != null)
                IconButton(onClick = { onEndIconClick?.invoke() }) {
                    Icon(imageVector = endIcon, contentDescription = null)
                }
        }
    }
}

@Composable
fun DisplayCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Surface(modifier = modifier
        .clickable { onClick.invoke() }) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            border = border
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                content = content
            )
        }
    }
}


fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Outlined.Edit,
    action: () -> Unit = {}
) {
    IconButton(
        onClick = action,
        modifier = modifier
    ) {
        Icon(imageVector = imageVector, contentDescription = "edit")
    }
}


@Composable
fun BottomPadding() {
    Spacer(
        modifier = Modifier.height(
            MaterialTheme.spacing.bottomPadding
        )
    )
}


fun LazyListScope.bottomPaddingLazy() {
    item(
        key = "bottom_padding"
    ) {
        BottomPadding()
    }
}

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: String,
    expand: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var isExpand by rememberSaveable { mutableStateOf(expand) }
    Column(
        modifier = Modifier
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isExpand,
                    onValueChange = { isExpand = it }
                )
                .padding(
                    horizontal = MaterialTheme.spacing.small
                )
                .height(
                    TextFieldDefaults.MinHeight
                )
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.captionColor,
            )
            Icon(
                imageVector = if (isExpand) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                contentDescription = null
            )
        }
        AnimatedVisibility(isExpand) {
            Column(
                content = content
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun TitleComposablePreview() {
    ResearchHubTheme {
        ExpandableCard(
            title = "Title",
            content = {
                Text(text = "Content")
            }
        )
    }
}

