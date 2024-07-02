package com.atech.ui_common.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@Composable
fun EducationDetailsItems(
    modifier: Modifier = Modifier,
    title: String,
    des: String,
    canShowButtons: Boolean = true,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = title, style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            Text(
                text = des,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.captionColor
            )
        }
        if (
            canShowButtons
        )
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                CustomIconButton(
                    action = onDeleteClick,
                    imageVector = Icons.Outlined.Delete
                )
                CustomIconButton(action = onEditClick)
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun EducationDetailsItemsPreview() {
    ResearchHubTheme {
        EducationDetailsItems(
            title = "This is only for experiment",
            des = "This is only for experiment"
        )
    }
}