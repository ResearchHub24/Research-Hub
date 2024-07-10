package com.atech.teacher.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.core.model.ResearchModel
import com.atech.teacher.ui.chat.AllChatViewModel
import com.atech.teacher.ui.chat.ChatViewModel
import com.atech.teacher.ui.research.ResearchViewModel
import com.atech.teacher.ui.research.compose.ResearchScreen
import com.atech.teacher.ui.send.SendNotificationViewModel
import com.atech.teacher.ui.send.compose.SendNotificationScreen
import com.atech.teacher.ui.student_profile.StudentProfileViewModel
import com.atech.teacher.ui.student_profile.compose.StudentProfileScreen
import com.atech.teacher.ui.view_applications.ViewApplicationViewModel
import com.atech.teacher.ui.view_applications.compose.ViewApplicationScreen
import com.atech.ui_common.common.chat.AllChatScreen
import com.atech.ui_common.common.chat.ChatScreen
import com.atech.ui_common.common.toast
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.animatedComposableEnh
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

sealed class ResearchRoutes(val route: String) {
    data object ResearchScreen : ResearchRoutes("research_screen")
    data object AllChats : ResearchRoutes("all_chat_screen")
    data object EditApplication : ResearchRoutes("edit_application")
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
data class ViewApplicationsArgs(
    val key: String, val selectedUser: String = ""
)

@Serializable
data class StudentProfileArgs(
    val uid: String
)

@Serializable
data class SendNotificationScreenArgs(
    val key: String, val title: String, val created: Long
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

fun AddEditScreenArgs.toResearchModel() = this.let { model ->
    ResearchModel(
        key = model.key,
        title = model.title,
        description = model.description,
        createdBy = model.createdBy,
        createdByUID = model.createdByUID,
        created = if (created == 0L) System.currentTimeMillis() else created,
        deadLine = if (deadLine == 0L) null else deadLine,
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
        addScreenNavGraph(navHostController)
        fadeThroughComposable(
            route = ResearchRoutes.ResearchScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navHostController)
            val research by viewModel.research.collectAsStateWithLifecycle(emptyList())

            ResearchScreen(
                navHostController = navHostController,
                state = research,
            )
        }

        animatedComposableEnh<ViewApplicationsArgs> { entry ->
            val viewModel: ViewApplicationViewModel = hiltViewModel()
            val args = entry.toRoute<ViewApplicationsArgs>()
            val submittedForms by viewModel.submittedForms
            ViewApplicationScreen(
                navController = navHostController,
                args = args,
                submittedForms = submittedForms,
                onEvent = viewModel::onEvent
            )
        }
        animatedComposableEnh<StudentProfileArgs> { entry ->
            val viewModel: StudentProfileViewModel = hiltViewModel()
            val args = entry.toRoute<StudentProfileArgs>()
            val userModel by viewModel.userModel
            StudentProfileScreen(
                uid = args.uid,
                navController = navHostController,
                setUID = viewModel::setUId,
                model = userModel
            )
        }
        animatedComposableEnh<SendNotificationScreenArgs> { entry ->
            val viewModel: SendNotificationViewModel = hiltViewModel()
            val args = entry.toRoute<SendNotificationScreenArgs>()
            val title by viewModel.title
            SendNotificationScreen(
                args = args,
                navController = navHostController,
                title = title,
                onEvent = viewModel::onEvent
            )
        }
        animatedComposable(
            route = ResearchRoutes.AllChats.route
        ) { entry ->
            val viewModel: AllChatViewModel = entry.sharedViewModel(navHostController)
            val allChats by viewModel.allChats.collectAsStateWithLifecycle(emptyList())
            AllChatScreen(navController = navHostController, state = allChats, onClick = {
                navHostController.navigate(
                    ChatScreenArgs(
                        path = it.path,
                        receiverUid = it.receiverUid,
                        receiverName = it.receiverName,
                        senderUid = it.senderUid,
                        senderName = it.senderName,
                        created = it.createdAt
                    )
                )
            })
        }
        animatedComposableEnh<ChatScreenArgs> { entry ->
            val args = entry.toRoute<ChatScreenArgs>()
            val viewModel: ChatViewModel = entry.sharedViewModel(navHostController)
            val chats by viewModel.getAllMessage(args.path).collectAsStateWithLifecycle(emptyList())
            var canSendMessage by rememberSaveable { mutableStateOf(true) }
            val context = LocalContext.current
            ChatScreen(
                title = args.receiverName,
                chats = chats.sortedByDescending { it.created },
                uid = args.senderUid,
                navController = navHostController,
                canSendMessage = canSendMessage,
                onSendClick = { message ->
                    canSendMessage = false
                    viewModel.sendMessage(
                        receiverName = args.receiverName,
                        receiverUid = args.receiverUid,
                        path = args.path,
                        message = message
                    ) { it ->
                        canSendMessage = true
                        if (it != null) {
                            runBlocking(Dispatchers.Main) {
                                toast(context, it.localizedMessage ?: "Some Error")
                            }
                            return@sendMessage
                        }
                        viewModel.sendPushNotification(
                            senderUid = args.receiverUid,
                            message = message,
                            title = args.senderName,
                            key = args.created
                        ) {
                            if (it != null) {
                                runBlocking(Dispatchers.Main) {
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
                }
            )
        }
    }
}