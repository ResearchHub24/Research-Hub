package com.atech.core.use_cases

import com.atech.core.retrofit.faculty.BASE_URL
import com.atech.core.retrofit.faculty.FacultyModel
import com.atech.core.retrofit.faculty.RetrofitApi
import com.atech.core.room.FacultyDao
import com.atech.core.room.model.FacultyMapper
import com.atech.core.utils.networkAccessBond
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import javax.inject.Inject

data class RetrofitUseCases @Inject constructor(
    val getAndSaveFacultiesUseCase: GetAndSaveFacultiesUseCase
)

data class GetAndSaveFacultiesUseCase @Inject constructor(
    private val getFacultiesUseCase: GetFacultiesUseCase,
    private val dao: FacultyDao,
    private val mapper: FacultyMapper
) {
    operator fun invoke() =
        networkAccessBond(
            networkCall = { getFacultiesUseCase() },
            dbCall = { dao.getFaculty() },
            saveCall = {
                dao.insertFaculty(mapper.mapFromEntityList(it))
            },
            mapper = { mapper.mapToEntityList(it) }
        )
}

data class GetFacultiesUseCase @Inject constructor(private val api: RetrofitApi) {
    suspend operator fun invoke() = getFacultyList(api.getPageResponse())

    private fun getFacultyList(context: String) =
        Jsoup.parse(context).select(".faculty-shortdesc")
            .map(::getFacultyDetails)

    private fun getFacultyDetails(e: Element): FacultyModel {
        val name = e.getElementsByClass("profile-name-faculty").firstOrNull()?.text() ?: ""
        val profileData =
            e.getElementsByClass("profile-data-faculty").firstOrNull()?.text() ?: ""
        val email = e.select(".profile-name-faculty-mail a").firstOrNull()?.attr("href")
            ?.removePrefix("mailto:") ?: ""
        val imageUrl = e.select(".faculty-image img").firstOrNull()?.attr("src") ?: ""
        val areaOfInterest =
            e.select(".faculty-info.area-interest p").firstOrNull()?.text() ?: ""
        val profileUrl = e.select(".view-profile-button a").firstOrNull()?.attr("href") ?: ""
        return FacultyModel(
            name = name,
            email = email,
            imageUrl = "$BASE_URL$imageUrl",
            profileData = profileData,
            areaOfInterest = areaOfInterest,
            profileUrl = if (profileUrl.isNotBlank()) "$BASE_URL$profileUrl" else ""
        )
    }
}