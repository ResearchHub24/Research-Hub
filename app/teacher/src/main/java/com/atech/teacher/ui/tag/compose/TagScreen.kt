package com.atech.teacher.ui.tag.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.TagModel
import com.atech.ui_common.R
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    tags: List<Pair<TagModel, Boolean>> = emptyList(),
    errorMessage: String? = null

) {
    var query by remember { mutableStateOf("") }
    MainContainer(
        modifier = modifier,
        title = stringResource(R.string.add_or_remove_tag),
        onNavigationClick = {
            navController.popBackStack()
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(
                    MaterialTheme.spacing.medium
                )
        ) {

            EditText(modifier = Modifier.fillMaxWidth(),
                value = query,
                placeholder = stringResource(R.string.tag),
                supportingMessage = stringResource(R.string.require),
                isError = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person, contentDescription = null
                    )
                },
                onValueChange = { value ->
                    query = value

                },
                clearIconClick = {
                    query = ""

                })
            AnimatedVisibility(
                visible = query.isNotEmpty()
            ) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                ApplyButton(
                    text = stringResource(R.string.create_new_tag),
                ) { }
            }
            AnimatedVisibility(
                visible = errorMessage != null
            ) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                DisplayCard(
                    modifier = Modifier,
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
                            text = errorMessage!!,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            AnimatedVisibility(
                visible = tags.isEmpty()
            ) {
                
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.medium
                )
            ) {
                items(tags.size) { index ->
                    TagItem(
                        item = tags[index]
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagScreenPreview() {
    ResearchHubTheme {
        TagScreen()
    }
}