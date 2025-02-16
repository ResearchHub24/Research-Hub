package com.atech.teacher.ui.view_applications.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.Action
import com.atech.core.model.QuestionModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.atech.core.utils.fromJsonList
import com.atech.ui_common.common.AppCustomAlertDialog
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.EditTextEnhance
import com.atech.ui_common.common.ExpandableCard
import com.atech.ui_common.common.ImageLoaderRounderCorner
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@Composable
fun ApplicationItem(
    modifier: Modifier = Modifier,
    model: ResearchPublishModel,
    onClick: () -> Unit = {},
    action: Action = Action.UNSELECTED,
    onViewProfileClick: () -> Unit = {},
    onActionClick: (Action) -> Unit = { _ -> }
) {
    var isDialogVisible by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(isDialogVisible) {
        ActionDialog(
            action = action,
            onActionClick = onActionClick,
            onDismissRequest = { isDialogVisible = false }
        )
    }
    Surface(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick.invoke() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    ImageLoaderRounderCorner(
                        modifier = Modifier.size(80.dp),
                        imageUrl = model.profileImg,
                        isRounderCorner = 100.dp
                    )
                    Spacer(Modifier.width(MaterialTheme.spacing.medium))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.8f)
                    ) {
                        Text(
                            text = model.studentName ?: "No Name",
                            maxLines = 1,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.small)
                                .basicMarquee()
                        )
                        Text(
                            maxLines = 1,
                            text = model.studentEmail ?: "No Email",
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.small)
                                .basicMarquee()
                        )
                        Text(
                            maxLines = 1,
                            text = model.studentPhoneNumber ?: "No Phone",
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.small)
                                .basicMarquee()
                        )
                    }

                }
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
                HorizontalDivider()
                ExpandableCard(title = "Response") {
                    val list = remember { fromJsonList<QuestionModel>(model.answers ?: "") }
                    list.forEach {
                        Spacer(Modifier.height(MaterialTheme.spacing.medium))
                        QuestionsItem(
                            model = it
                        )
                    }
                }
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
                HorizontalDivider()
                Text(
                    text = "Filled Date : ${model.filledDate.convertLongToTime(DateFormat.DD_MMM_YYYY.format)}",
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small)
                        .basicMarquee(),
                    style = MaterialTheme.typography.labelSmall
                )
                Row {
                    ApplyButton(
                        modifier = Modifier.weight(.5f),
                        text = "Action",
                        action = {
                            isDialogVisible = true
                        }
                    )
                    ApplyButton(
                        modifier = Modifier.weight(.5f),
                        text = "View Profile",
                        action = onViewProfileClick
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionsItem(
    modifier: Modifier = Modifier, model: QuestionModel
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        EditTextEnhance(
            modifier = Modifier.fillMaxWidth(),
            value = model.question ?: "",
            onValueChange = {},
            placeholder = "Question",
            enable = false,
            trailingIcon = null,
            colors = questionItemTextFieldColor().copy(
                disabledContainerColor = TextFieldDefaults.colors().unfocusedContainerColor,
                disabledLabelColor = TextFieldDefaults.colors().unfocusedLabelColor,
                disabledIndicatorColor = TextFieldDefaults.colors().unfocusedIndicatorColor
            )
        )

        EditTextEnhance(
            modifier = Modifier.fillMaxWidth(),
            value = model.answer ?: "",
            onValueChange = {},
            placeholder = "Answer",
            enable = false,
            trailingIcon = null,
            colors = questionItemTextFieldColor()
        )
    }
}

@Composable
private fun questionItemTextFieldColor() = TextFieldDefaults.colors(
    disabledContainerColor = TextFieldDefaults.colors().focusedContainerColor,
    disabledLabelColor = TextFieldDefaults.colors().focusedLabelColor,
    disabledTextColor = TextFieldDefaults.colors().focusedTextColor,
    disabledIndicatorColor = TextFieldDefaults.colors().focusedIndicatorColor
)


@Composable
private fun ActionDialog(
    modifier: Modifier = Modifier,
    action: Action = Action.UNSELECTED,
    onActionClick: (Action) -> Unit = { _ -> },
    onDismissRequest: () -> Unit = {}
) {
    var clickedAction by rememberSaveable { mutableStateOf(action) }
    AppCustomAlertDialog(
        modifier = modifier
            .fillMaxWidth(),
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AlertDialogDefaults.containerColor,
            ), shape = AlertDialogDefaults.shape, elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = AlertDialogDefaults.TonalElevation
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .padding(vertical = MaterialTheme.spacing.large)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Action",
                    color = AlertDialogDefaults.titleContentColor
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                Action.entries.forEach { it ->
                    Row(
                        modifier = Modifier
                            .padding(
                                MaterialTheme.spacing.medium,
                            )
                            .fillMaxWidth()
                            .clickable {
                                clickedAction = it
                            },
                    ) {
                        RadioButton(
                            selected = (it.name == clickedAction.name),
                            onClick = null
                        )
                        Text(
                            text = it.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = MaterialTheme.spacing.large),
                            color = AlertDialogDefaults.textContentColor
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(
                        modifier = Modifier.weight(.5f),
                        onClick = {
                            onActionClick.invoke(clickedAction)
                            onDismissRequest.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = AlertDialogDefaults.iconContentColor
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(.5f),
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = null,
                            tint = AlertDialogDefaults.iconContentColor
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ApplicationItemPreview() {
    ResearchHubTheme {
        ApplicationItem(
            model = ResearchPublishModel(
                studentName = "Ayaan",
                studentEmail = "aiyuayaan1909@gmail.com",
                studentPhoneNumber = "1234567890",
                filledDate = 1719909208783,
                answers = "[{\"answer\":\"Google is very big company yoyoyoyo yoy oyo y oy oy oy oyo yo Google is very big company yoyoyoyo yoy oyo y oy oy oy oyo yo Google is very big company yoyoyoyo yoy oyo y oy oy oy oyo yo Google is very big company yoyoyoyo yoy oyo y oy oy oy oyo yo Google is very big company yoyoyoyo yoy oyo y oy oy oy oyo yo \",\"question\":\"What is Google\"},{\"answer\":\"Python is a programming language Save changes and send push notifications to all users?Save changes and send push notifications to all users?Save changes and send push notifications to all users?Save changes and send push notifications to all users?\",\"question\":\"What is Python\"}]"
            )
        )
//        ActionDialog()
//        QuestionsItem(
//            model = QuestionModel(
//                question = "What is google",
//                answer = "Google is a Cow" // only sarcastic answers lol !!
//            )
//        )
    }
}