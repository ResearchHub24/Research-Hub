package com.atech.teacher.ui.view_applications.compose

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
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.QuestionModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.atech.core.utils.fromJsonList
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
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
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
                    ) {

                    }
                    ApplyButton(
                        modifier = Modifier.weight(.5f),
                        text = "View Profile",
                    ) {

                    }
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
            colors = questionItemTextFieldColor()
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
//        QuestionsItem(
//            model = QuestionModel(
//                question = "What is google",
//                answer = "Google is a Cow" // only sarcastic answers lol !!
//            )
//        )
    }
}