package com.atech.teacher.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.teacher.ui.add.AddOrEditViewModel
import com.atech.teacher.ui.add.compose.AddEditScreen
import com.atech.teacher.ui.add.compose.AddQuestionScreen
import com.atech.teacher.ui.add.compose.ViewMarkdown
import com.atech.teacher.ui.tag.TagViewModel
import com.atech.teacher.ui.tag.compose.TagScreen
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.animatedComposableEnh
import com.atech.ui_common.utils.sharedViewModel
import kotlinx.serialization.Serializable

sealed class EditRoutes(val route: String) {
    data object AddQuestionScreen : EditRoutes("add_or_edit_screen")
    data object AddTagsScreen : EditRoutes("add_tags_screen")
}

@Serializable
data class ViewMarkdownArgs(
    val markdown: String
)

@Serializable
data class AddEditScreenArgs(
    val key: String? = "",
    val title: String = "N/A",
    val description: String = "N/A",
    val createdBy: String = "N/A",
    val createdByUID: String = "N/A",
    val created: Long = 0L,
    val deadLine: Long = 0L,
    val tags: String = "N/A",
    val questions: String = "N/A"
)

infix fun AddEditScreenArgs.areEqual(other: AddEditScreenArgs) =
    this.key == other.key && this.title == other.title && this.description == other.description && this.createdBy == other.createdBy && this.createdByUID == other.createdByUID && this.created == other.created && this.deadLine == other.deadLine && this.tags == other.tags && this.questions == other.questions

fun NavGraphBuilder.addScreenNavGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = EditRoutes.AddQuestionScreen.route,
        route = ResearchRoutes.EditApplication.route
    ) {
        animatedComposableEnh<AddEditScreenArgs> { entry ->
            val viewModel: AddOrEditViewModel =
                entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val state by viewModel.state
            val title = state.title
            val des = state.description
            val tags = state.tags
            val question = state.questions
            AddEditScreen(
                navHostController = navHostController,
                title = title,
                description = des,
                tags = tags,
                question = question,
                onEvent = viewModel::onEvent,
            )
        }
        animatedComposable(
            route = EditRoutes.AddTagsScreen.route
        ) { entry ->
            val viewModel: TagViewModel = hiltViewModel()
            val addOrEditViewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val tags by viewModel.tags
            val errorMessage by viewModel.errorMessage
            val addEditScreenState = addOrEditViewModel.state.value.tags
            val selectedTags = addOrEditViewModel.getTagsFromString(addEditScreenState)
            TagScreen(
                navController = navHostController,
                tags = tags,
                errorMessage = errorMessage,
                onEvent = viewModel::onEvent,
                selectedTags = selectedTags,
                onTagChangeEvents = addOrEditViewModel::onEvent
            )
        }
        animatedComposableEnh<ViewMarkdownArgs> { entry ->
            val args = entry.toRoute<ViewMarkdownArgs>()
            ViewMarkdown(
                navController = navHostController, args = args
            )
        }
        animatedComposable(
            route = EditRoutes.AddQuestionScreen.route
        ) { entry ->
            val addOrEditViewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val questionsJson = addOrEditViewModel.state.value.questions
            AddQuestionScreen(
                navController = navHostController,
                questionsJson = questionsJson,
                onEvent = addOrEditViewModel::onEvent
            )
        }
    }
}