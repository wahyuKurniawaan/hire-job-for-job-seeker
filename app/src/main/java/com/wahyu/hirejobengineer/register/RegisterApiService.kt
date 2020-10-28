package com.wahyu.hirejobengineer.register

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterApiService {

    @FormUrlEncoded
    @POST("users/register")
    suspend fun registerRequest(
        @Field("user_name") name: String?,
        @Field("user_email") email: String?,
        @Field("user_password") password: String?,
        @Field("phone_number") phoneNumber: String?,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("profile-job-seeker")
    suspend fun createProfileJobSeeker(
        @Field("user_id") userId: Int?
    ): CreateProfileResponse

    data class CreateProfileResponse(val success: Boolean, val message: String?)

}