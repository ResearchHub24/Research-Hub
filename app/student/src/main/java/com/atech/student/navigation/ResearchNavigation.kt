package com.atech.student.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.student.ui.question.QuestionsViewModel
import com.atech.student.ui.question.compose.QuestionScreen
import com.atech.student.ui.research.detail.compose.ResearchDetailScreen
import com.atech.student.ui.research.main.ResearchScreenEvents
import com.atech.student.ui.research.main.ResearchViewModel
import com.atech.student.ui.research.main.compose.ResearchScreen
import com.atech.student.ui.resume.ResumeViewModel
import com.atech.student.ui.resume.compose.AddEditScreen
import com.atech.student.ui.resume.compose.AllApplicationScreen
import com.atech.student.ui.resume.compose.ResumeScreen
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.animatedComposableEnh
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel
import kotlinx.serialization.Serializable

sealed class ResearchScreenRoutes(
    val route: String,
) {
    data object ResearchScreen : ResearchScreenRoutes("research_screen")
    data object DetailScreen : ResearchScreenRoutes("detail_screen")
    data object ResumeScreen : ResearchScreenRoutes("resume_screen")
    data object EditScreen : ResearchScreenRoutes("edit_screen")
    data object AllApplicationScreen : ResearchScreenRoutes("all_application_screen")
}

@Serializable
data class ResumeScreenArgs(
    val key: String, val question: String, val fromDetailScreen: Boolean = true
)


@Serializable
/***
 * triple : (name,email,phone)
 */
data class QuestionScreenArgs(
    val userName: String,
    val userEmail: String,
    val userPhone: String,
    val key: String,
    val question: String,
    val filledForm: String
)

fun NavGraphBuilder.researchScreenGraph(
    navController: NavHostController, navigateToLogIn: () -> Unit, logOut: () -> Unit
) {
    navigation(
        route = RouteName.RESEARCH.value,
        startDestination = ResearchScreenRoutes.ResearchScreen.route
    ) {
        fadeThroughComposable(
            route = ResearchScreenRoutes.ResearchScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navController = navController)
            val resumeViewModel =
                entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val items = viewModel.research.collectAsState(initial = emptyList())
            ResearchScreen(
                items = items.value,
                navController = navController,
                onEvent = viewModel::onEvent,
                resumeEvents = resumeViewModel::onEvent
            )
        }

        animatedComposable(
            route = ResearchScreenRoutes.DetailScreen.route + "?key={key}",
            arguments = listOf(navArgument("key") {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }),
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navController = navController)
            entry.arguments?.getString("key")?.let {
                viewModel.onEvent(ResearchScreenEvents.SetDataFromArgs(it))
            }
            val resumeViewModel =
                entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val filledForm by resumeViewModel.filledForm
            entry.arguments?.getString("key")?.let {
                viewModel.onEvent(ResearchScreenEvents.SetDataFromArgs(it))
            }
            val clickedItem by viewModel.clickItem
            val isExistInWishList by viewModel.isExist
            val isFromArgs by viewModel.isFromArgs
            ResearchDetailScreen(
                onEvent = viewModel::onEvent,
                navController = navController,
                model = clickedItem ?: return@animatedComposable,
                isExistInWishList = isExistInWishList,
                isFromArgs = isFromArgs,
                filledForm = filledForm,
                isUserLogIn = viewModel.isUserLogIn
            )
        }
        animatedComposable(
            route = ResearchScreenRoutes.ResumeScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val state by viewModel.resumeState
            val args = ResumeScreenArgs(
                key = "", question = "", fromDetailScreen = false
            )
            ResumeScreen(
                state = state,
                navController = navController,
                onEvents = viewModel::onEvent,
                args = args,
                isUserLogIn = viewModel.isUserLogIn,
                navigateToLogIn = navigateToLogIn,
                logOut = logOut
            )
        }
        animatedComposableEnh<ResumeScreenArgs> { entry ->
            val viewModel = entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val state by viewModel.resumeState
            val args = entry.toRoute<ResumeScreenArgs>()
            ResumeScreen(
                state = state,
                navController = navController,
                onEvents = viewModel::onEvent,
                args = args,
                isUserLogIn = viewModel.isUserLogIn,
                navigateToLogIn = navigateToLogIn
            )
        }
        animatedComposable(
            route = ResearchScreenRoutes.EditScreen.route,
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val state by viewModel.addScreenState
            AddEditScreen(
                navController = navController, state = state, onEvent = viewModel::onEvent
            )
        }
        animatedComposableEnh<QuestionScreenArgs> { entry ->
            val viewModel = entry.sharedViewModel<QuestionsViewModel>(navController = navController)
            val args = entry.toRoute<QuestionScreenArgs>()
            viewModel.setArgs(
                args
            )
            val state by viewModel.questionsState
            QuestionScreen(
                navController = navController, state = state, onEvent = viewModel::onEven
            )
        }
        animatedComposable(
            route = ResearchScreenRoutes.AllApplicationScreen.route
        ) { entry ->
            val researchViewModel =
                entry.sharedViewModel<ResearchViewModel>(navController = navController)
            val resumeViewModel =
                entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val appApplication by researchViewModel.research.collectAsState(emptyList())
            val resumeState by resumeViewModel.resumeState
            val filledForm = resumeState.userData.filledForm ?: ""
            val selectedForm = resumeState.userData.selectedForm ?: ""
            val filledApplication = appApplication.filter { list ->
                filledForm.contains(list.key ?: "")
            }.map { filteredList ->
                filteredList to selectedForm.contains(filteredList.key ?: "")
            }
            AllApplicationScreen(
                navHostController = navController, state = filledApplication
            )
        }
    }
}