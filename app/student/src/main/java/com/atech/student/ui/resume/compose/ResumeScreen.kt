package com.atech.student.ui.resume.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.EducationDetails
import com.atech.core.utils.fromJsonList
import com.atech.student.navigation.HomeScreenRoutes
import com.atech.student.navigation.QuestionScreenArgs
import com.atech.student.navigation.ResearchScreenRoutes
import com.atech.student.navigation.ResumeScreenArgs
import com.atech.student.ui.resume.ResumeScreenEvents
import com.atech.student.ui.resume.ResumeState
import com.atech.ui_common.R
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.CardSection
import com.atech.ui_common.common.CustomIconButton
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EducationDetailsItems
import com.atech.ui_common.common.ImageLoaderRounderCorner
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.TextItem
import com.atech.ui_common.common.bottomPaddingLazy
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    modifier: Modifier = Modifier,
    isUserLogIn: Boolean = true,
    state: ResumeState = ResumeState(),
    navController: NavHostController = rememberNavController(),
    args: ResumeScreenArgs = ResumeScreenArgs("", ""),
    navigateToLogIn: () -> Unit = {},
    onEvents: (ResumeScreenEvents) -> Unit = {},
    logOut: () -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val context = LocalContext.current
    val canFillApplication = state.userData.phone.isNullOrBlank() ||
            state.userData.educationDetails.isNullOrBlank() ||
            state.userData.educationDetails == "[]"
    if (isUserLogIn.not()) {
        LaunchedEffect(lifecycleState) {
            when (lifecycleState) {
                Lifecycle.State.RESUMED -> onEvents(ResumeScreenEvents.UpdateUserDetails)

                else -> {}
            }
        }
    }
    val navIcon = if (args.fromDetailScreen) {
        {
            navController.popBackStack()
            Unit
        }
    } else null

    MainContainer(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = stringResource(if (args.fromDetailScreen) R.string.resume else R.string.profile),
        scrollBehavior = scrollBehavior,
        onNavigationClick = navIcon
    ) { contentPadding ->
        if (isUserLogIn.not()) {
            LogInScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                navController.popBackStack(
                    HomeScreenRoutes.HomeScreen.route, inclusive = false
                )
                navigateToLogIn()
            }
            return@MainContainer
        }
        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            item(key = "about") {
                CardSection(title = stringResource(R.string.personal_details)) {
                    if (args.fromDetailScreen.not()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                        ) {
                            ImageLoaderRounderCorner(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxHeight(),
                                imageUrl = state.userData.photoUrl,
                                isRounderCorner = 100.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                text = state.userData.phone ?: "Add phone number",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.captionColor
                            )
                        }
                        CustomIconButton {
                            onEvents(ResumeScreenEvents.OnPersonalDetailsClick)
                            navController.navigate(ResearchScreenRoutes.EditScreen.route)
                        }
                    }
                    if (state.userData.phone.isNullOrBlank()) {
                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                        DisplayCard(
                            modifier = Modifier.fillMaxSize(),
                            border = BorderStroke(
                                width = CardDefaults.outlinedCardBorder().width,
                                color = MaterialTheme.colorScheme.error
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
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
                    if (args.fromDetailScreen.not() && state.userData.filledForm.isNullOrBlank()
                            .not()
                    ) {
                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                        AddButton(
                            title = stringResource(R.string.view_all_applications),
                            imageVector = Icons.AutoMirrored.Outlined.LibraryBooks
                        ) {
                            navController.navigate(ResearchScreenRoutes.AllApplicationScreen.route)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            item(key = "education") {
                CardSection(
                    title = stringResource(R.string.education)
                ) {
                    state.userData.educationDetails?.let { educationDetails ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                        ) {
                            fromJsonList<EducationDetails>(educationDetails).sortedByDescending { it.startYear.toInt() }
                                .forEachIndexed { index, item ->
                                    EducationDetailsItems(title = "${item.degree}, ${item.startYear} - ${
                                        item.endYear ?: stringResource(
                                            R.string.present
                                        )
                                    } ${item.percentage.let { if (it?.toDouble() == 0.0) "" else "( $it )" }}",
                                        des = item.institute,
                                        onDeleteClick = {
                                            onEvents(ResumeScreenEvents.OnEducationSave(
                                                pos = index
                                            ) { message ->
                                                if (message != null) {
                                                    toast(context, message)
                                                    return@OnEducationSave
                                                }
                                                toast(
                                                    context,
                                                    context.getString(R.string.deleted_successfully)
                                                )
                                            })
                                        },
                                        onEditClick = {
                                            onEvents(
                                                ResumeScreenEvents.OnAddEditEducationClick(
                                                    item, index
                                                )
                                            )
                                            navController.navigate(ResearchScreenRoutes.EditScreen.route)
                                        })
                                }
                        }
                    }
                    AnimatedVisibility(
                        state.userData.educationDetails.isNullOrBlank() ||
                                state.userData.educationDetails == "[]"
                    ) {
                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.small))
                        DisplayCard(
                            modifier = Modifier.fillMaxSize(),
                            border = BorderStroke(
                                width = CardDefaults.outlinedCardBorder().width,
                                color = MaterialTheme.colorScheme.error
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = stringResource(R.string.education_details_are_require),
                                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    AddButton(
                        title = stringResource(R.string.add_education),
                    ) {
                        onEvents(ResumeScreenEvents.OnAddEditEducationClick())
                        navController.navigate(ResearchScreenRoutes.EditScreen.route)
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            item(key = "skills") {
                CardSection(
                    title = stringResource(R.string.skills)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        state.userData.skillList?.let { skillList ->
                            fromJsonList<String>(skillList).forEachIndexed { index, item ->
                                TextItem(text = item,
                                    endIcon = Icons.Outlined.Delete,
                                    onEndIconClick = {
                                        onEvents.invoke(ResumeScreenEvents.OnSkillClick(
                                            skill = item, pos = index
                                        ) { message ->
                                            if (message != null) {
                                                toast(context, message)
                                                return@OnSkillClick
                                            }
                                            toast(
                                                context,
                                                context.getString(R.string.deleted_successfully)
                                            )
                                        })
                                    })
                            }
                        }
                    }
                    AddButton(
                        title = stringResource(R.string.add_skills),
                    ) {
                        onEvents(ResumeScreenEvents.OnAddSkillClick)
                        navController.navigate(ResearchScreenRoutes.EditScreen.route)
                    }
                }
                Spacer(modifier = Modifier.size(MaterialTheme.spacing.large))
            }
            if (args.fromDetailScreen) {
                item(key = "apply") {
                    ApplyButton(
                        text = stringResource(R.string.proceed_to_application),
                        enable = canFillApplication.not(),
                        action = {
                            navController.navigate(
                                QuestionScreenArgs(
                                    key = args.key,
                                    userEmail = state.userData.email,
                                    question = args.question,
                                    userName = state.userData.name,
                                    userPhone = state.userData.phone ?: "N/A",
                                    filledForm = state.userData.filledForm ?: "[]",
                                )
                            )
                        }
                    )
                }
            }
            if (args.fromDetailScreen.not()) {
                item(key = "log_out") {
                    AddButton(title = stringResource(R.string.log_out),
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        action = {
                            logOut()
                        })
                }
            }
            bottomPaddingLazy()
        }
    }
}

@Composable
private fun AddButton(
    title: String, imageVector: ImageVector = Icons.Outlined.Edit, action: () -> Unit = {}
) {
    TextButton(
        onClick = action, modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
            imageVector = imageVector,
            contentDescription = title
        )
        Text(text = title)
    }
}



@Composable
fun LogInScreen(
    modifier: Modifier = Modifier, loginClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        DisplayCard(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .align(Alignment.Center),
            border = BorderStroke(
                width = CardDefaults.outlinedCardBorder().width,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.please_login_to_view_your_resume))
                AddButton(
                    title = "Log In", Icons.AutoMirrored.Outlined.Login, action = loginClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResumeScreenPreview() {
    ResearchHubTheme {
        ResumeScreen(
            isUserLogIn = false
        )
    }
}