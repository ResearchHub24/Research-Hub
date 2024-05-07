package com.atech.student.ui.research.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.atech.core.model.ResearchModel
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchItem
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    items: List<ResearchModel> = emptyList()
) {
    MainContainer(
        title = stringResource(id = R.string.research),
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(items.size) { index ->
                ResearchItem(model = items[index])
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResearchScreenPreview() {
    ResearchHubTheme {
        ResearchScreen()
    }
}