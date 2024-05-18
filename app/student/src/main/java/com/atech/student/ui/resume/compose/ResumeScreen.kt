package com.atech.student.ui.resume.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.EducationDetails
import com.atech.core.utils.fromJsonList
import com.atech.student.ui.resume.ResumeState
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    modifier: Modifier = Modifier,
    state: ResumeState = ResumeState(),
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = stringResource(R.string.resume),
        scrollBehavior = scrollBehavior,
        onNavigationClick = {
            navController.popBackStack()
        }) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            item(key = "about") {
                CardSection(title = "About") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = state.userData.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                            Text(
                                text = state.userData.email,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                            Text(
                                text = state.userData.phone ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.captionColor
                            )
                        }
                        EditButton()
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            item(key = "education") {
                CardSection(
                    title = "Education"
                ) {
                    state.userData.educationDetails?.let { educationDetails ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            fromJsonList<EducationDetails>(educationDetails).forEach { item ->
                                EducationDetailsItems(
                                    title = "${item.degree} ${item.startYear} - ${item.endYear ?: "Present"} (${item.percentage})",
                                    des = item.institute
                                )
                            }
//                        item(key = "add_education") {
                            AddButton(
                                title = "Add Education",
                            )
//                        }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            item(key = "skills") {
                CardSection(
                    title = "Skills"
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        AddButton(
                            title = "Add Skills",
                        )
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            item(key = "apply") {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(MaterialTheme.spacing.medium)
                ) {
                    Text(
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        text = "Proceed to application"
                    )
                }
            }
        }
    }
}

@Composable
private fun AddButton(
    title: String,
    action: () -> Unit = {}
) {
    TextButton(
        onClick = action,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
            imageVector = Icons.Outlined.Edit,
            contentDescription = title
        )
        Text(text = title)
    }
}

@Composable
private fun EducationDetailsItems(
    modifier: Modifier = Modifier,
    title: String,
    des: String,
    onEditClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
            Text(
                text = des,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.captionColor
            )
        }
        EditButton(action = onEditClick)
    }
}

@Composable
private fun EditButton(
    action: () -> Unit = {}
) {
    IconButton(onClick = action) {
        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
    }
}

@Composable
fun CardSection(
    modifier: Modifier = Modifier, title: String, content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            .5.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.captionColor
            )
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            content()
            Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResumeScreenPreview() {
    ResearchHubTheme {
        ResumeScreen()
    }
}