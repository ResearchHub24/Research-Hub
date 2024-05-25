package com.atech.ui_common.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.retrofit.FacultyModel
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing



@Composable
fun TeacherItem(
    modifier: Modifier = Modifier,
    model: FacultyModel
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = isExpanded,
                onValueChange = { isExpanded = it }
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring()
                )
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                ),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.name,
                                style = if (isExpanded) MaterialTheme.typography.titleMedium
                                else MaterialTheme.typography.titleSmall,
                            )
                        }

                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Work,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.profileData,
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                            )
                        }
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AlternateEmail,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.email,
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                    ImageLoaderRounderCorner(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = spring()
                            )
                            .height(if (isExpanded) 120.dp else 80.dp),

                        imageUrl = model.imageUrl,
                        isRounderCorner = 100.dp
                    )
                }
                AnimatedVisibility(
                    isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = model.areaOfInterest,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .weight(.9f)
                        )

                        FloatingActionButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.small),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.OpenInBrowser,
                                contentDescription = null,
                            )
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherItemPreview() {
    ResearchHubTheme {
        TeacherItem(
            model = FacultyModel(
                name = "Dr. Aparna Shukla",
                email = "a.shukla@bitmesra.ac.in",
                profileData = "Associate Lecturer",
                imageUrl = "https://www.bitmesra.ac.in//UploadedDocuments/useraparnashukla/FacultyImage/FacImge3d56051a77946439fbc22d89da4e18cDr Aparna.jpg",
                areaOfInterest = "Programming Languages, Data Structures and Algorithms, Data Mining, Machine Learning, Biometrics, Computer Graphics",
                profileUrl = ""
            )
        )
    }
}