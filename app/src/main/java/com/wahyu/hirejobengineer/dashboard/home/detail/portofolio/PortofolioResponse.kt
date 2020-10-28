package com.wahyu.hirejobengineer.dashboard.home.detail.portofolio

import com.google.gson.annotations.SerializedName

data class PortofolioResponse(val success: Boolean, val message: String?, val data: List<DataResult>?) {

    data class DataResult(
        @SerializedName("id_portofolio") val idPortofolio: Int?,
        @SerializedName("id_profile_job_seeker") val idJobSeeker: Int?,
        @SerializedName("application_name") val appName: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("publication_link") val publishLink: String?,
        @SerializedName("repository_link") val repoLink: String?,
        @SerializedName("workplace") val workplace: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("portofolio_image") val portoImage: String?,
    )
}