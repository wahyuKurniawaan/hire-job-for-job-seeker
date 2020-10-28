package com.wahyu.hirejobengineer.dashboard.profile

import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApiService {

    @GET("profile-job-seeker/")
    suspend fun getProfileJobSeekerByIdRequest(
        @Query("search[user_id]") userId: Int,
    ): ProfileResponse

}