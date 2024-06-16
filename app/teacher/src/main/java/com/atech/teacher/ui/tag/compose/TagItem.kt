package com.atech.teacher.ui.tag.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.TagModel
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    item: Pair<TagModel, Boolean> = Pair(TagModel(name = "Tag 1"), false),
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var isChecked by remember { mutableStateOf(checked) }
    val (tag, isCreatedByLoggedUser) = item
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = isChecked, onValueChange = {
                    isChecked = it
                    onCheckedChange(it)
                }
            )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .height(40.0.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked, onCheckedChange = {
                        isChecked = it
                        onCheckedChange(it)
                    }
                )
                Text(
                    modifier = Modifier
                        .weight(.7f)
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    text = tag.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (isCreatedByLoggedUser) IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagItemPreview() {
    ResearchHubTheme {
        TagItem()
    }
}