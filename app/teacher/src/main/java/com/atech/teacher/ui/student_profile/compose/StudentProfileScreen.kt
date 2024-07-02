package com.atech.teacher.ui.student_profile.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.EMPTY_USER
import com.atech.core.model.EducationDetails
import com.atech.core.model.StudentUserModel
import com.atech.core.utils.fromJsonList
import com.atech.ui_common.R
import com.atech.ui_common.common.CardSection
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EducationDetailsItems
import com.atech.ui_common.common.ImageLoaderRounderCorner
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ProgressBar
import com.atech.ui_common.common.TextItem
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    uid: String,
    setUID: (String) -> Unit = {},
    model: StudentUserModel?
) {
    val context = LocalContext.current
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        setUID.invoke(uid)
    }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = "",
        onNavigationClick = {
            navController.navigateUp()
        }) { paddingValues ->
        if (model == null) {
            toast(context, "Unable to find user details")
            return@MainContainer
        }
        if (model.uid.isEmpty()) {
            ProgressBar(paddingValues)
            return@MainContainer
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = paddingValues
        ) {
            aboutSection(model)
            educationSection(model)
            skillSection(model)
        }
    }
}

private fun LazyListScope.aboutSection(model: StudentUserModel) {
    item(key = "about") {
        CardSection(title = stringResource(R.string.personal_details)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            ) {
                ImageLoaderRounderCorner(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight(),
                    imageUrl = model.photoUrl,
                    isRounderCorner = 100.dp
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = model.name, style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                    Text(
                        text = model.email, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                    Text(
                        text = model.phone ?: "Add phone number",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.captionColor
                    )
                }
            }
            if (model.phone.isNullOrBlank()) {
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                DisplayCard(
                    modifier = Modifier.fillMaxSize(),
                    border = BorderStroke(
                        width = CardDefaults.outlinedCardBorder().width,
                        color = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = stringResource(R.string.phone_number_is_required),
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
    }
}

private fun LazyListScope.educationSection(model: StudentUserModel) {
    item(key = "education") {
        CardSection(
            title = stringResource(R.string.education)
        ) {
            model.educationDetails?.let { educationDetails ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    fromJsonList<EducationDetails>(educationDetails).sortedByDescending { it.startYear.toInt() }
                        .forEach { item ->
                            EducationDetailsItems(
                                title = "${item.degree}, ${item.startYear} - ${
                                    item.endYear ?: stringResource(
                                        R.string.present
                                    )
                                } ${item.percentage.let { if (it?.toDouble() == 0.0) "" else "( $it )" }}",
                                des = item.institute,
                                canShowButtons = false
                            )
                        }
                }
            }
        }
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
    }
}

private fun LazyListScope.skillSection(model: StudentUserModel) {
    item(key = "skills") {
        CardSection(
            title = stringResource(R.string.skills)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                model.skillList?.let { skillList ->
                    fromJsonList<String>(skillList).forEach { item ->
                        TextItem(
                            text = item
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ResearchHubTheme {
        StudentProfileScreen(
            uid = "", model = EMPTY_USER.copy(uid = "Fsd")
        )
    }
}