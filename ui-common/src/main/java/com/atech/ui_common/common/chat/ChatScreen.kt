package com.atech.ui_common.common.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.MessageModel
import com.atech.core.utils.getDate
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    title: String = "",
    uid: String = "",
    canSendMessage: Boolean = true,
    longPressEnable: Boolean = true,
    navController: NavController = rememberNavController(),
    chats: List<MessageModel> = emptyList(),
    onSendClick: (String) -> Unit = {},
    onDeleteClick: (String) -> Unit = {}
) {
    MainContainer(modifier = modifier.imePadding(), title = title, onNavigationClick = {
        navController.navigateUp()
    }, bottomBar = {
        ChatBox(
            canSendMessage = canSendMessage, onSendClick = onSendClick
        )
    }) { paddingValues ->
        if (chats.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues), title = ""
            )
            return@MainContainer
        }
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            items(chats) {
                ChatItem(
                    model = it,
                    uid = uid,
                    longPressEnable = longPressEnable,
                    onDeleteClick = {
                        onDeleteClick.invoke(it.path)
                    }
                )
            }
        }
    }
}

val drawerColor: Color
    @Composable get() = ColorUtils.blendARGB(
        MaterialTheme.colorScheme.surface.toArgb(), MaterialTheme.colorScheme.primary.toArgb(), .09f
    ).let { Color(it) }

@Composable
fun ChatBox(
    canSendMessage: Boolean = true, onSendClick: (String) -> Unit = {}
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(Modifier.width(MaterialTheme.spacing.small))
        OutlinedTextField(
            modifier = Modifier.weight(.7f),
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            placeholder = {
                Text(text = "Message")
            },
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = drawerColor,
                unfocusedContainerColor = drawerColor,
            )
        )
        IconButton(
            onClick = {
                onSendClick.invoke(chatBoxValue.text)
                chatBoxValue = TextFieldValue("")
            },
            Modifier
                .padding(start = MaterialTheme.spacing.small)
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
                .weight(0.1f),
            enabled = chatBoxValue.text.isNotBlank() && canSendMessage
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    model: MessageModel,
    uid: String = "",
    longPressEnable: Boolean = true,
    onDeleteClick: () -> Unit = {}
) {
    var isDeleteVisible by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .apply {
                if (longPressEnable) combinedClickable(onLongClickLabel = stringResource(R.string.delete),
                    onLongClick = {
                        isDeleteVisible = !isDeleteVisible
                    },
                    onClick = {
                        if (isDeleteVisible) isDeleteVisible = false
                    })
            }
            .fillMaxWidth(),
    ) {
        val isUserSender = model.senderUid == uid
        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(if (isUserSender) Alignment.CenterEnd else Alignment.CenterStart)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (isUserSender) 48f else 0f,
                        bottomEnd = if (isUserSender) 0f else 48f
                    )
                )
                .background(
                    if (isUserSender) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                )

        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            ) {
                Text(
                    text = model.message,
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = model.created.getDate().trim(),
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .5f)
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
                AnimatedVisibility(isDeleteVisible) {
                    IconButton(
                        onClick = onDeleteClick, modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ResearchHubTheme {
        ChatItem(
            model = MessageModel(
                senderName = "Ayaan",
                receiverName = "Jai",
                message = "This is for Information",
                senderUid = "abc"
            ), uid = "abc"
        )
//        ChatScreen(
//            title = "Jay",
//            chats = listOf()
//        )
    }
}
