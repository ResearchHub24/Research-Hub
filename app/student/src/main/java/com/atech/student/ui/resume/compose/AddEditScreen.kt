package com.atech.student.ui.resume.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.student.ui.resume.AddEditScreenType
import com.atech.student.ui.resume.AddScreenState
import com.atech.student.ui.resume.ResumeScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.EditTextEnhance
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.TextItem
import com.atech.ui_common.common.TitleComposable
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing
import kotlinx.coroutines.launch
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: AddScreenState = AddScreenState(),
    onEvent: (ResumeScreenEvents) -> Unit = {}
) {
    val context = LocalContext.current
    val title = when (state.screenType) {
        AddEditScreenType.DETAILS -> context.getString(R.string.personal_details)
        AddEditScreenType.EDUCATION -> context.getString(R.string.education)
        AddEditScreenType.SKILL -> context.getString(R.string.skills)
    }
    MainContainer(modifier = modifier, title = title, onNavigationClick = {
        navController.popBackStack()
    }) { paddingValue ->
        when (state.screenType) {
            AddEditScreenType.DETAILS ->
                EditPersonalDetails(
                    modifier = Modifier.padding(paddingValue),
                    model = state.personalDetails,
                    onEvent = onEvent
                )

            AddEditScreenType.EDUCATION ->
                AddOrEditEducation(
                    modifier = Modifier.padding(paddingValue),
                )


            AddEditScreenType.SKILL -> AddSkillList(
                modifier = Modifier.padding(paddingValue),
                skillList = state.skillList,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun EditPersonalDetails(
    modifier: Modifier = Modifier,
    model: Triple<String, String, String>,
    onEvent: (ResumeScreenEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        EditText(modifier = Modifier.fillMaxWidth(),
            value = model.first,
            placeholder = stringResource(R.string.name),
            supportingMessage = stringResource(R.string.enter_your_full_name),
            errorMessage = stringResource(R.string.name_is_required),
            isError = model.first.isEmpty(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = value, phone = model.third
                    )
                )
            },
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = "", phone = model.third
                    )
                )
            })
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        EditText(
            modifier = Modifier.fillMaxWidth(),
            value = model.second,
            placeholder = stringResource(R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email, contentDescription = null
                )
            },
            trailingIcon = null,
            enable = false,
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        EditText(modifier = Modifier.fillMaxWidth(),
            value = model.third,
            placeholder = stringResource(R.string.contact_number),
            trailingIcon = null,
            leadingIcon = null,
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = model.first, phone = value
                    )
                )
            },
            isError = model.third.isEmpty(),
            errorMessage = stringResource(R.string.phone_number_is_required),
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = model.first, phone = ""
                    )
                )
            })
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        ApplyButton(
            text = stringResource(R.string.save_details)
        ) {

        }
    }
}

private enum class Request {
    START_YEAR, END_YEAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditEducation(
    modifier: Modifier = Modifier
) {
    val yearList =
        (1990..Calendar.getInstance().get(Calendar.YEAR)).map { it.toString() }.reversed()
    var startYearSelection by remember { mutableStateOf("") }
    var endYearSelection by remember { mutableStateOf("") }
    var isCurrentlyLearning by remember { mutableStateOf(false) }
    var request by remember { mutableStateOf(Request.START_YEAR) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
            ) {
                items(yearList) { title ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            )
                            .clickable {
                                when (request) {
                                    Request.START_YEAR -> startYearSelection = title
                                    Request.END_YEAR -> endYearSelection = title
                                }
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            },
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.medium)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        EditText(modifier = Modifier.fillMaxWidth(),
            value = "",
            placeholder = "Collage/School",
            supportingMessage = "require",
            isError = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            onValueChange = { value ->

            },
            clearIconClick = {}
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        TitleComposable(
            title = "Year Details"
        )
        EditTextEnhance(modifier = Modifier
            .fillMaxWidth(),
            value = startYearSelection,
            placeholder = "Start Year",
            trailingIcon = null,
            readOnly = true,
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            request = Request.START_YEAR
                            showBottomSheet = true
                        }
                    }
                }
            })
        EditTextEnhance(modifier = Modifier
            .fillMaxWidth(),
            value = endYearSelection,
            placeholder = "End Year",
            trailingIcon = null,
            readOnly = true,
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource, isCurrentlyLearning) {
                    interactionSource.interactions.collect {
                        if (!isCurrentlyLearning)
                            if (it is PressInteraction.Release) {
                                request = Request.START_YEAR
                                showBottomSheet = true
                            }
                    }
                }
            })
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(checked = isCurrentlyLearning,
                onCheckedChange = { value ->
                    isCurrentlyLearning = value
                    endYearSelection = if (value) "Present" else ""
                })
            Text(text = "Current Pursuing")
        }
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        TitleComposable(
            title = "Grades"
        )
        EditTextEnhance(modifier = Modifier.fillMaxWidth(),
            value = "",
            placeholder = "Grades/Percentage",
            isError = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            onValueChange = { value ->
            },
            clearIconClick = {}
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        ApplyButton(text = "Add Education") {
            // TODO add education
        }
    }
}

@Composable
fun AddSkillList(
    modifier: Modifier = Modifier,
    skillList: List<String>,
    onEvent: (ResumeScreenEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        var query by remember { mutableStateOf("") }
        EditText(modifier = Modifier.fillMaxWidth(),
            value = query,
            placeholder = "Skill",
            supportingMessage = "require",
            isError = false,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            onValueChange = { value ->
                query = value
                onEvent.invoke(ResumeScreenEvents.FilterResult(value))
            },
            clearIconClick = {}
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(skillList) {
                TextItem(text = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
        AddSkillList(skillList = listOf(), onEvent = {})
    }
}