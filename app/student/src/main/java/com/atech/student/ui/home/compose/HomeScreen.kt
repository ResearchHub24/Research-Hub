package com.atech.student.ui.home.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.atech.core.model.UserModel
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ProfileImage
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    model: UserModel? = null
) {
    MainContainer(
        title = stringResource(id = R.string.home),
        modifier = modifier,
        actions = {
            ProfileImage(
                url = model?.photoUrl,
                onClick = { }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier,
            contentPadding = paddingValues
        ) {
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ResearchHubTheme {
        HomeScreen(

        )
    }
}