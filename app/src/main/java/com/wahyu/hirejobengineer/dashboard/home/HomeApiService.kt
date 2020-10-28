package com.wahyu.hirejobengineer.dashboard.home

import com.wahyu.hirejobengineer.dashboard.home.detail.portofolio.PortofolioResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerRequest(): HomeResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByJobTitleRequest(@Query("search[job_title]") search: String): HomeResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByIdRequest(@Query("search[id_profile_job_seeker]") id: Int): HomeResponse

    @GET("portofolio-job-seeker/")
    suspend fun getPortofolioJobSeekerByIdRequest(@Query("search[id_profile_job_seeker]") id: Int) : PortofolioResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByNameRequest(@Query("search[user_name]") search: String): HomeResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerBySkillRequest(@Query("search[skill]") search: String): HomeResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByLocationRequest(@Query("search[city]") search: String): HomeResponse

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByStatusJobRequest(@Query("search[status_job]") search: String): HomeResponse

}