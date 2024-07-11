package com.atech.student.navigation

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.student.ui.chat.AllChatViewModel
import com.atech.student.ui.chat.ChatViewModel
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
import com.atech.ui_common.common.chat.AllChatScreen
import com.atech.ui_common.common.chat.ChatScreen
import com.atech.ui_common.common.toast
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
    data object AllChats : ResearchScreenRoutes("all_chat_screen")
}

@Serializable
data class ChatScreenArgs(
    val path: String,
    val senderName: String,
    val senderUid: String,
    val receiverName: String,
    val receiverUid: String,
    val created: Long
)


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
            val items by viewModel.researchWithDataState
            ResearchScreen(
                items = items,
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
            val resumeViewModel =
                entry.sharedViewModel<ResumeViewModel>(navController = navController)
            val appApplication by resumeViewModel.research.collectAsState(emptyList())
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
        animatedComposable(
            route = ResearchScreenRoutes.AllChats.route
        ) { entry ->
            val viewModel: AllChatViewModel = entry.sharedViewModel(navController)
            val allChats by viewModel.allChats.collectAsStateWithLifecycle(emptyList())
            AllChatScreen(
                navController = navController, state = allChats, onClick = {
                    navController.navigate(
                        ChatScreenArgs(
                            path = it.path,
                            receiverUid = it.receiverUid,
                            receiverName = it.receiverName,
                            senderUid = it.senderUid,
                            senderName = it.senderName,
                            created = it.createdAt
                        ),
                    )
                }, forAdmin = false
            )
        }
        animatedComposableEnh<ChatScreenArgs> { entry ->
            val args = entry.toRoute<ChatScreenArgs>()
            val viewModel: ChatViewModel = entry.sharedViewModel(navController)
            val chats by viewModel.getAllMessage(args.path).collectAsStateWithLifecycle(emptyList())
            var canSendMessage by rememberSaveable { mutableStateOf(true) }
            val context = LocalContext.current
            ChatScreen(title = args.senderName,
                chats = chats.sortedByDescending { it.created },
                uid = args.receiverUid,
                navController = navController,
                canSendMessage = canSendMessage,
                longPressEnable = false,
                onSendClick = { message ->
                    canSendMessage = false
                    viewModel.sendMessage(
                        receiverName = args.senderName,
                        receiverUid = args.senderUid,
                        path = args.path,
                        message = message
                    ) { it ->
                        canSendMessage = true
                        if (it != null) {
                            Handler(Looper.getMainLooper()).post {
                                toast(context, it.localizedMessage ?: "Some Error")
                            }
                            return@sendMessage
                        }
                        viewModel.sendPushNotification(
                            senderUid = args.senderUid,
                            message = message,
                            title = args.receiverName,
                            key = args.created
                        ) {
                            if (it != null) {
                                Handler(Looper.getMainLooper()).post {
                                    toast(context, it.localizedMessage ?: "Some Error")
                                }
                                return@sendPushNotification
                            }
                        }
                    }
                },
                onDeleteClick = {
                    viewModel.deleteMessage(
                        rootPath = args.path,
                        docPath = it,
                    ) { error ->
                        if (error != null) {
                            toast(context, error.localizedMessage ?: "Some Error")
                            return@deleteMessage
                        }
                        toast(context, "Message Deleted")
                    }
                })
        }
    }
}