package com.atech.teacher.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.core.model.ResearchModel
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.teacher.ui.add.AddOrEditViewModel
import com.atech.teacher.ui.add.compose.AddEditScreen
import com.atech.teacher.ui.add.compose.AddQuestionScreen
import com.atech.teacher.ui.add.compose.ViewMarkdown
import com.atech.teacher.ui.research.ResearchViewModel
import com.atech.teacher.ui.research.compose.ResearchScreen
import com.atech.teacher.ui.tag.TagViewModel
import com.atech.teacher.ui.tag.compose.TagScreen
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.animatedComposableEnh
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel
import kotlinx.serialization.Serializable

sealed class ResearchRoutes(val route: String) {
    data object ResearchScreen : ResearchRoutes("research_screen")
    data object AddQuestionScreen : ResearchRoutes("add_or_edit_screen")
    data object AddTagsScreen : ResearchRoutes("add_tags_screen")
}

@Serializable
data class ViewMarkdownArgs(
    val markdown: String
)

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

infix fun AddEditScreenArgs.areEqual(other: AddEditScreenArgs) =
    this.key == other.key &&
            this.title == other.title &&
            this.description == other.description &&
            this.createdBy == other.createdBy &&
            this.createdByUID == other.createdByUID &&
            this.created == other.created &&
            this.deadLine == other.deadLine &&
            this.tags == other.tags &&
            this.questions == other.questions


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

fun AddEditScreenArgs.toResearchModel() = this.let { model ->
    ResearchModel(
        key = model.key,
        title = model.title,
        description = model.description,
        createdBy = model.createdBy,
        createdByUID = model.createdByUID,
        created = if (created == 0L) System.currentTimeMillis() else created,
        deadLine = if (created == 0L) null else deadLine,
        tags = model.tags,
        questions = model.questions
    )
}

fun NavGraphBuilder.researchScreenGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = ResearchRoutes.ResearchScreen.route,
        route = MainScreenRoutes.ResearchScreen.route
    ) {
        fadeThroughComposable(
            route = ResearchRoutes.ResearchScreen.route
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
        animatedComposableEnh<AddEditScreenArgs> { entry ->
            val args = entry.toRoute<AddEditScreenArgs>()
            val viewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            viewModel.onEvent(AddEditScreenEvent.SetArgs(args))
            val title by viewModel.title
            val des by viewModel.description
            val tags by viewModel.tags
            val question by viewModel.question
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
            route = ResearchRoutes.AddTagsScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<TagViewModel>(navHostController)
            val addOrEditViewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val tags by viewModel.tags
            val errorMessage by viewModel.errorMessage
            val addEditScreenState by addOrEditViewModel.state
            val selectedTags = addOrEditViewModel.getTagsFromString(addEditScreenState.tags)
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
                navController = navHostController,
                args = args
            )
        }
        animatedComposable(
            route = ResearchRoutes.AddQuestionScreen.route
        ) { entry ->
            val addOrEditViewModel = entry.sharedViewModel<AddOrEditViewModel>(navHostController)
            val questionsJson by addOrEditViewModel.question
            AddQuestionScreen(
                navController = navHostController,
                questionsJson = questionsJson,
                onEvent = addOrEditViewModel::onEvent
            )
        }
    }
}