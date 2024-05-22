package com.atech.student.ui.resume.compose

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.EducationDetails
import com.atech.student.ui.resume.AddEditScreenType
import com.atech.student.ui.resume.AddScreenState
import com.atech.student.ui.resume.ResumeScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.EditTextEnhance
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.TextItem
import com.atech.ui_common.common.TitleComposable
import com.atech.ui_common.common.toast
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
            AddEditScreenType.DETAILS -> EditPersonalDetails(
                modifier = Modifier.padding(paddingValue),
                model = state.personalDetails,
                onEvent = onEvent,
                navController = navController,
                context = context
            )

            AddEditScreenType.EDUCATION -> AddOrEditEducation(
                modifier = Modifier.padding(paddingValue),
                state = state.details,
                onEvent = onEvent,
                navController = navController,
                context = context
            )


            AddEditScreenType.SKILL -> AddSkillList(
                modifier = Modifier.padding(paddingValue),
                skillList = state.skillList,
                onEvent = onEvent,
                navController = navController,
                context = context
            )
        }
    }
}

@Composable
private fun EditPersonalDetails(
    modifier: Modifier = Modifier,
    model: Triple<String, String, String>,
    onEvent: (ResumeScreenEvents) -> Unit,
    navController: NavHostController,
    context: Context
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        EditText(
            modifier = Modifier.fillMaxWidth(),
            value = model.first,
            placeholder = stringResource(R.string.name),
            supportingMessage = stringResource(R.string.enter_your_full_name),
            maxLines = 1,
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
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            )
        )
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
        EditText(
            modifier = Modifier.fillMaxWidth(),
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
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone
            )
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        ApplyButton(
            text = stringResource(R.string.save_details)
        ) {
            onEvent(
                ResumeScreenEvents.OnPersonalDataSave { message ->
                    if (message != null) {
                        toast(context, message)
                        return@OnPersonalDataSave
                    }
                    toast(
                        context,
                        context.getString(R.string.personal_information_is_updated)
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}

private enum class Request {
    START_YEAR, END_YEAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditEducation(
    modifier: Modifier = Modifier,
    state: EducationDetails,
    onEvent: (ResumeScreenEvents) -> Unit,
    navController: NavHostController,
    context: Context
) {
    val yearList =
        (2010..Calendar.getInstance().get(Calendar.YEAR)).map { it.toString() }.reversed()
    var isCurrentlyLearning by remember { mutableStateOf(false) }
    var request by remember { mutableStateOf(Request.START_YEAR) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val isYearValid = state.endYear?.let { year ->
        if (year == context.getString(
                R.string.present
            )
        ) true
        else if (year.isNotBlank() && state.startYear.toInt() <= year.toInt()) true
        else false
    } ?: false

    val isGradeValid = state.percentage?.let { per ->
        per.isNotBlank() && per.toDouble() <= 100.0
    } ?: false

    val hasError =
        state.institute.isEmpty() || state.degree.isEmpty() || state.startYear.isEmpty()
                || state.endYear.isNullOrEmpty() || state.percentage.isNullOrEmpty()
                || !isYearValid || !isGradeValid


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
                                    Request.START_YEAR -> onEvent.invoke(
                                        ResumeScreenEvents.OnEducationEdit(
                                            model = state.copy(
                                                startYear = title
                                            )
                                        )
                                    )

                                    Request.END_YEAR -> onEvent.invoke(
                                        ResumeScreenEvents.OnEducationEdit(
                                            model = state.copy(
                                                endYear = title
                                            )
                                        )
                                    )
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
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TitleComposable(
            title = stringResource(R.string.education_details)
        )
        EditTextEnhance(
            modifier = Modifier.fillMaxWidth(),
            value = state.institute,
            placeholder = stringResource(R.string.collage_school),
            isError = state.institute.isEmpty(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person, contentDescription = null
                )
            },
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnEducationEdit(
                        model = state.copy(
                            institute = value
                        )
                    )
                )
            },
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnEducationEdit(
                        model = state.copy(
                            institute = ""
                        )
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            )
        )
        EditTextEnhance(
            modifier = Modifier.fillMaxWidth(),
            value = state.degree,
            placeholder = stringResource(R.string.degree_subject),
            isError = state.degree.isEmpty(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Book, contentDescription = null
                )
            },
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnEducationEdit(
                        model = state.copy(
                            degree = value
                        )
                    )
                )
            },
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnEducationEdit(
                        model = state.copy(
                            degree = ""
                        )
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        TitleComposable(
            title = stringResource(R.string.year_details)
        )
        AnimatedVisibility(
            isYearValid.not()
        ) {
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
                        text = stringResource(R.string.end_year_should_be_greater_than_start_year),
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        EditTextEnhance(modifier = Modifier.fillMaxWidth(),
            value = state.startYear,
            placeholder = stringResource(R.string.start_year),
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
        EditTextEnhance(modifier = Modifier.fillMaxWidth(),
            value = state.endYear ?: "",
            placeholder = stringResource(R.string.end_year),
            trailingIcon = null,
            readOnly = true,
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource, isCurrentlyLearning) {
                    interactionSource.interactions.collect {
                        if (!isCurrentlyLearning) if (it is PressInteraction.Release) {
                            request = Request.END_YEAR
                            showBottomSheet = true
                        }
                    }
                }
            })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(value = isCurrentlyLearning, onValueChange = { value ->
                    isCurrentlyLearning = value
                    onEvent.invoke(
                        ResumeScreenEvents.OnEducationEdit(
                            model = state.copy(
                                endYear = if (value) context.getString(
                                    R.string.present
                                ) else "",
                                percentage = if (value) "0" else null
                            )
                        )
                    )
                }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(checked = isCurrentlyLearning, onCheckedChange = { value ->
                isCurrentlyLearning = value
                onEvent.invoke(
                    ResumeScreenEvents.OnEducationEdit(
                        model = state.copy(
                            endYear = if (value) context.getString(
                                R.string.present
                            ) else "",
                            percentage = if (value) "0" else null
                        )
                    )
                )
            })
            Text(text = stringResource(R.string.current_pursuing))
        }
        AnimatedVisibility(isCurrentlyLearning.not()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                TitleComposable(
                    title = stringResource(R.string.grades)

                )
                AnimatedVisibility(
                    isGradeValid.not()
                ) {
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
                                text = stringResource(R.string.grade_should_be_less_than_100),
                                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    value = if (state.percentage == null) "" else state.percentage.toString(),
                    placeholder = stringResource(R.string.grades_percentage),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person, contentDescription = null
                        )
                    },
                    onValueChange = { value ->
                        onEvent.invoke(
                            ResumeScreenEvents.OnEducationEdit(
                                model = state.copy(percentage = value)
                            )
                        )
                    },
                    clearIconClick = {
                        onEvent.invoke(
                            ResumeScreenEvents.OnEducationEdit(
                                model = state.copy(percentage = null)
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        ApplyButton(
            text = stringResource(R.string.add_education), enable = hasError.not()
        ) {
            onEvent(
                ResumeScreenEvents.OnEducationSave { message ->
                    if (message != null) {
                        toast(context, message)
                        return@OnEducationSave
                    }
                    toast(
                        context,
                        context.getString(R.string.education_details_added)
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun AddSkillList(
    modifier: Modifier = Modifier,
    skillList: List<String>,
    onEvent: (ResumeScreenEvents) -> Unit,
    navController: NavHostController,
    context: Context
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        var query by remember { mutableStateOf("") }
        EditText(modifier = Modifier.fillMaxWidth(),
            value = query,
            placeholder = stringResource(R.string.skill),
            supportingMessage = stringResource(R.string.require),
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
            clearIconClick = {})
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(skillList) {
                TextItem(
                    text = it,
                    onClick = {
                        onEvent.invoke(
                            ResumeScreenEvents.OnSkillClick(
                                it
                            ) { message ->
                                if (message != null) {
                                    toast(context, message)
                                    return@OnSkillClick
                                }
                                toast(
                                    context,
                                    context.getString(R.string.skill_added)
                                )
                                navController.popBackStack()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
    }
}