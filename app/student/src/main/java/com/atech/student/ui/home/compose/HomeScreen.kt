package com.atech.student.ui.home.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.StudentUserModel
import com.atech.student.navigation.ResearchScreenRoutes
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ProfileImage
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    model: StudentUserModel? = null,
    navigateToLogIn: () -> Unit={},
    navController: NavHostController = rememberNavController()
) {
    MainContainer(
        title = stringResource(id = R.string.home),
        modifier = modifier,
        actions = {
            ProfileImage(
                url = model?.photoUrl,
                onClick = {
                    if (model != null) {
                        navController.navigate(
                            ResearchScreenRoutes.ResumeScreen.route
                        )
                    } else {
                        navigateToLogIn.invoke()
                    }
                }
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