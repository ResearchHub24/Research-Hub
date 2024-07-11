package com.atech.core.use_cases

import com.atech.core.model.TagModel
import com.atech.core.pref.StudentPrefManager
import javax.inject.Inject

data class StudentPrefUseCases @Inject constructor(
    val getAllPrefStudentUseCase: GetAllPrefStudentUseCase,
    val updateFilterStudentUseCase: UpdateFilterStudentUseCase
)

data class GetAllPrefStudentUseCase @Inject constructor(
    private val manager: StudentPrefManager
) {
    operator fun invoke() = manager.studentPref
}

data class UpdateFilterStudentUseCase @Inject constructor(
    private val manager: StudentPrefManager
) {
    suspend operator fun invoke(filter: List<TagModel>) = manager.updateFilter(filter)
}