package com.wahyu.hirejobengineer.dashboard.offers

import com.wahyu.hirejobengineer.dashboard.offers.offersBy.ProfileRecruiterResponse
import retrofit2.http.*

interface OffersApiService {

    @GET("offers/")
    suspend fun getOffersListRequest(@Query("search[user.user_id]") userId: Int?): OffersResponse

    @GET("profile-recruiter/")
    suspend fun getProfileRecruiterByEmailRequest(
        @Query("search[user.user_email]") emailRecruiter: String?
    ): ProfileRecruiterResponse

    @FormUrlEncoded
    @PUT("offers/{id}")
    suspend fun editStatusRequest(
        @Path("id") id: Int?,
        @Field("status") status: String?
    ) : EditResponse
}