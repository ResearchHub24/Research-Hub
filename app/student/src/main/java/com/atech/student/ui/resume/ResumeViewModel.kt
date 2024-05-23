package com.atech.student.ui.resume

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.config.RemoteConfigHelper
import com.atech.core.model.EducationDetails
import com.atech.core.use_cases.AuthUseCases
import com.atech.core.utils.RemoteConfigKeys
import com.atech.core.utils.TAGS
import com.atech.core.utils.UserLoggedIn
import com.atech.core.utils.fromJsonList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val conf: RemoteConfigHelper,
    @UserLoggedIn val isUserLogIn: Boolean
) : ViewModel() {
    private val _resumeState = mutableStateOf(ResumeState())
    val resumeState: State<ResumeState> get() = _resumeState
    private val _filledForm = mutableStateOf("")
    val filledForm: State<String> get() = _filledForm

    private val _addScreenState = mutableStateOf(AddScreenState())
    val addScreenState: State<AddScreenState> get() = _addScreenState
    private var educationClickItemPos: Int? = null


    init {
        updateUserDetails()
    }

    private fun fetchDataFromRemoteConfig() {
        conf.fetchData(failure = {
            Log.e(TAGS.ERROR.name, "fetchDataFromRemoteConfig: $it")
        }) {
            _addScreenState.value = _addScreenState.value.copy(
                skillList = fromJsonList<String>(conf.getString(RemoteConfigKeys.SKILLS.value))
            )
        }
    }

    private fun updateUserDetails() {
        viewModelScope.launch {
            authUseCases.getUserDetailsUseFromAuthCase(
                fromDatabase = true
            )?.let {
                _resumeState.value = _resumeState.value.copy(
                    userData = it
                ).also { details ->
                    _filledForm.value = details.userData.filledForm ?: ""
                }
            }
        }
    }

    fun onEvent(event: ResumeScreenEvents) {
        when (event) {
            ResumeScreenEvents.OnPersonalDetailsClick -> {
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.DETAILS, personalDetails = Triple(
                        first = _resumeState.value.userData.name,
                        second = _resumeState.value.userData.email,
                        third = _resumeState.value.userData.phone ?: ""
                    )
                )
            }

            is ResumeScreenEvents.OnPersonalDataEdit -> {
                _addScreenState.value = addScreenState.value.copy(
                    personalDetails = Triple(
                        first = event.name,
                        second = _resumeState.value.userData.email,
                        third = event.phone
                    )
                )
            }

            ResumeScreenEvents.UpdateUserDetails -> updateUserDetails()

            is ResumeScreenEvents.OnAddEditEducationClick -> {
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.EDUCATION,
                ).let { addScreenState ->
                    event.model?.let { m ->
                        educationClickItemPos = event.position
                        addScreenState.copy(
                            details = m
                        )
                    } ?: addScreenState
                }
            }

            is ResumeScreenEvents.OnEducationEdit -> {
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.EDUCATION,
                    details = _addScreenState.value.details.copy(
                        institute = event.model.institute,
                        degree = event.model.degree,
                        startYear = event.model.startYear,
                        endYear = event.model.endYear,
                        percentage = event.model.percentage
                    )
                )
            }

            ResumeScreenEvents.OnAddSkillClick -> {
                fetchDataFromRemoteConfig()
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.SKILL,
                )
            }

            is ResumeScreenEvents.FilterResult -> {
                _addScreenState.value =
                    _addScreenState.value.copy(skillList = if (event.query.isBlank()) fromJsonList<String>(
                        conf.getString(
                            RemoteConfigKeys.SKILLS.value
                        )
                    )
                    else _addScreenState.value.skillList.filter {
                        it.contains(event.query, ignoreCase = true)
                    })
            }

            is ResumeScreenEvents.OnPersonalDataSave -> viewModelScope.launch {
                authUseCases.saveDetails.saveProfileData(name = addScreenState.value.personalDetails.first,
                    phone = addScreenState.value.personalDetails.third,
                    onComplete = { exception ->
                        event.onComplete.invoke(exception?.message)
                    })
                updateUserDetails()
            }


            is ResumeScreenEvents.OnEducationSave -> viewModelScope.launch {
                val educationList = fromJsonList<EducationDetails>(
                    _resumeState.value.userData.educationDetails ?: ""
                ).toMutableList()
                if (event.pos == null) {
                    if (educationClickItemPos == null) educationList.add(
                        _addScreenState.value.details
                    )
                    else {
                        educationList[educationClickItemPos!!] = _addScreenState.value.details
                    }
                } else {
                    educationList.removeAt(event.pos)
                }
                authUseCases.saveDetails.saveEducationData(educationDetails = educationList,
                    onComplete = { exception ->
                        educationClickItemPos = null
                        event.onComplete.invoke(exception?.message)
                    })
                updateUserDetails()
            }


            is ResumeScreenEvents.OnSkillClick -> viewModelScope.launch {
                val skillList = fromJsonList<String>(
                    _resumeState.value.userData.skillList ?: ""
                ).toMutableList()
                if (event.pos == null) skillList.add(
                    event.skill
                )
                else skillList.removeAt(event.pos)
                authUseCases.saveDetails.saveSkillData(skillList = skillList,
                    onComplete = { exception ->
                        event.onComplete.invoke(exception?.message)
                    })
                updateUserDetails()
            }

            ResumeScreenEvents.ResetState -> {
                _addScreenState.value = AddScreenState()
                educationClickItemPos = null
            }
        }
    }
}