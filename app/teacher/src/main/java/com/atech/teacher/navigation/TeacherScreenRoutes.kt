package com.atech.teacher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.atech.core.model.ResearchModel
import com.atech.teacher.ui.add.AddOrEditViewModel
import com.atech.teacher.ui.add.compose.AddEditScreen
import com.atech.teacher.ui.profile.compose.ProfileScreen
import com.atech.teacher.ui.research.ResearchViewModel
import com.atech.teacher.ui.research.compose.ResearchScreen
import com.atech.teacher.ui.tag.TagViewModel
import com.atech.teacher.ui.tag.compose.TagScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.fadeThroughComposableEnh
import com.atech.ui_common.utils.sharedViewModel
import kotlinx.serialization.Serializable

sealed class TeacherScreenRoutes(
    val route: String
) {
    data object ResearchScreen : TeacherScreenRoutes("research_screen")
    data object ProfileScreen : TeacherScreenRoutes("profile_screen")
    data object TagScreen : TeacherScreenRoutes("tag_screen")
}

@Serializable
data class AddEditScreenArgs(
    val key: String?,
    val title: String = "N/A",
    val description: String = "N/A",
    val createdBy: String = "N/A",
    val createdByUID: String = "N/A",
    val created: Long = 0L,
    val deadLine: Long = 0L,
    val tags: String = "N/A",
    val questions: String = "N/A"
)

fun AddEditScreenArgs.replaceNA() = this.copy(
    title = if (title == "N/A") "" else title,
    description = if (description == "N/A") "" else description,
    createdBy = if (createdBy == "N/A") "" else createdBy,
    createdByUID = if (createdByUID == "N/A") "" else createdByUID,
    tags = if (tags == "N/A") "" else tags,
    questions = if (questions == "N/A") "" else questions
)

fun ResearchModel.fromResearchModel() = this.let { model ->
    AddEditScreenArgs(
        key = model.key,
        title = model.title ?: "N/A",
        description = model.description ?: "N/A",
        createdBy = model.createdBy ?: "N/A",
        createdByUID = model.createdByUID ?: "N/A",
        created = model.created ?: 0L,
        deadLine = model.deadLine ?: 0L,
        tags = model.tags ?: "[ ]",
        questions = model.questions ?: "[ ]"
    )
}

@Composable
fun MainScreenTeacherNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = TeacherScreenRoutes.TagScreen.route,
    logOut: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        fadeThroughComposable(
            route = TeacherScreenRoutes.ResearchScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navHostController)
            val research by viewModel.research.collectAsStateWithLifecycle(emptyList())
            val verifyScreenState by viewModel.verifyScreenState
            ResearchScreen(
                navHostController = navHostController,
                state = research,
                verifyScreen = verifyScreenState,
                onVerifyScreenEvent = viewModel::onVerifyScreenEvent
            )
        }
        fadeThroughComposableEnh<AddEditScreenArgs> { entry ->
            val args = entry.toRoute<AddEditScreenArgs>()
            val viewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val state by viewModel.state
            AddEditScreen(
                navHostController = navHostController,
                state = state,
                onEvent = viewModel::onEvent,
                args = args
            )
        }
        fadeThroughComposable(
            route = TeacherScreenRoutes.ProfileScreen.route
        ) {
            ProfileScreen(
                logOut = logOut
            )
        }
        fadeThroughComposable(
            route = TeacherScreenRoutes.TagScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<TagViewModel>(navHostController)
            val tags by viewModel.tags
            val errorMessage by viewModel.errorMessage
            TagScreen(
                navController = navHostController,
                tags = tags,
                errorMessage = errorMessage
            )
        }
    }
}
