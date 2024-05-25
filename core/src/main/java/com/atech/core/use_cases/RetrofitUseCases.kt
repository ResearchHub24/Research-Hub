package com.atech.core.use_cases

import com.atech.core.retrofit.BASE_URL
import com.atech.core.retrofit.FacultyModel
import com.atech.core.retrofit.RetrofitApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import javax.inject.Inject

data class RetrofitUseCases @Inject constructor(
    val getFacultiesUseCase: GetFacultiesUseCase
)


data class GetFacultiesUseCase @Inject constructor(private val api: RetrofitApi) {
    suspend operator fun invoke() = getFacultyList(api.getPageResponse())

    private fun getFacultyList(context: String) =
        Jsoup.parse(context).select(".faculty-shortdesc")
            .map(::getFacultyDetails)

    private fun getFacultyDetails(e: Element): FacultyModel {
        val name = e.getElementsByClass("profile-name-faculty").firstOrNull()?.text() ?: "N/A"
        val profileData =
            e.getElementsByClass("profile-data-faculty").firstOrNull()?.text() ?: "N/A"
        val email = e.select(".profile-name-faculty-mail a").firstOrNull()?.attr("href")
            ?.removePrefix("mailto:") ?: "N/A"
        val imageUrl = e.select(".faculty-image img").firstOrNull()?.attr("src") ?: "N/A"
        val areaOfInterest =
            e.select(".faculty-info.area-interest p").firstOrNull()?.text() ?: "N/A"
        val profileUrl = e.select(".view-profile-button a").firstOrNull()?.attr("href") ?: "N/A"
        return FacultyModel(
            name = name,
            email = email,
            imageUrl = "$BASE_URL$imageUrl",
            profileData = profileData,
            areaOfInterest = areaOfInterest,
            profileUrl = "$BASE_URL$profileUrl"
        )
    }
}