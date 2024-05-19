package com.atech.student.ui.resume.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.student.ui.resume.AddEditScreenType
import com.atech.student.ui.resume.AddScreenState
import com.atech.student.ui.resume.ResumeScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing


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
    MainContainer(
        modifier = modifier,
        title = title,
        onNavigationClick = {
            navController.popBackStack()
        }
    ) { paddingValue ->
        when (state.screenType) {
            AddEditScreenType.DETAILS -> {
                EditPersonalDetails(
                    modifier = Modifier.padding(paddingValue),
                    model = state.personalDetails,
                    onEvent = onEvent
                )
            }

            AddEditScreenType.EDUCATION -> {}
            AddEditScreenType.SKILL -> {}
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
        EditText(
            modifier = Modifier.fillMaxWidth(),
            value = model.first,
            placeholder = stringResource(com.atech.student.R.string.name),
            supportingMessage = stringResource(com.atech.student.R.string.enter_your_full_name),
            errorMessage = stringResource(com.atech.student.R.string.name_is_required),
            isError = model.first.isEmpty(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = value,
                        phone = model.third
                    )
                )
            },
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = "",
                        phone = model.third
                    )
                )
            }
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        EditText(
            modifier = Modifier.fillMaxWidth(),
            value = model.second,
            placeholder = stringResource(com.atech.student.R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null
                )
            },
            trailingIcon = null,
            enable = false,
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        EditText(
            modifier = Modifier.fillMaxWidth(),
            value = model.third,
            placeholder = stringResource(com.atech.student.R.string.contact_number),
            trailingIcon = null,
            leadingIcon = null,
            onValueChange = { value ->
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = model.first,
                        phone = value
                    )
                )
            },
            isError = model.third.isEmpty(),
            errorMessage = stringResource(com.atech.student.R.string.phone_number_is_required),
            clearIconClick = {
                onEvent.invoke(
                    ResumeScreenEvents.OnPersonalDataEdit(
                        name = model.first,
                        phone = ""
                    )
                )
            }
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        ApplyButton(
            text = stringResource(com.atech.student.R.string.save_details)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
    }
}