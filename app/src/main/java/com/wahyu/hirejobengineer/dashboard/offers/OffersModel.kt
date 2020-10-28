package com.wahyu.hirejobengineer.dashboard.offers

data class OffersModel(
    val id: Int?,
    val userId: Int?,
    val name: String?,
    val projectDescription: String?,
    val price: Long?,
    val duration: String?,
    val image: String?,
    val status: String?,
    val offeredBy: String?,
)