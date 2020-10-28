package com.wahyu.hirejobengineer.dashboard.offers.offersBy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahyu.hirejobengineer.dashboard.offers.EditResponse
import com.wahyu.hirejobengineer.dashboard.offers.OffersApiService
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OfferedByViewModel : ViewModel(), CoroutineScope {

    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isProfileLiveData = MutableLiveData<Boolean>()
    val isEditStatusLiveData = MutableLiveData<Boolean>()
    val listLiveData = MutableLiveData<List<ProfileRecruiterModel>>()

    private lateinit var service: OffersApiService
    private lateinit var sharedPref: SharedPreferencesUtil

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setProfileService(service: OffersApiService) {
        this.service = service
    }

    fun setSharedPref(sharePref: SharedPreferencesUtil) {
        this.sharedPref = sharePref
    }

    fun callProfileApi(email: String) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getProfileRecruiterByEmailRequest(email)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Job() + Dispatchers.Main) {
                        isProfileLiveData.value = false
                    }
                }
            }
            if (response is ProfileRecruiterResponse) {
                val list = response.data?.map {
                    ProfileRecruiterModel(it.id, it.userId, it.name, it.email, it.companyName, it.roleJob, it.phoneNumber, it.userRole,
                        it.profileImage, it.companyField, it.city, it.description, it.instagramLink, it.linkedinLink)
                } ?: listOf()
                listLiveData.value = list
                isProfileLiveData.value = true
            }
            isLoadingLiveData.value = false
        }
    }

    fun callEditStatusApi(id: Int?, status: String?) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.editStatusRequest(id, status)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Job() + Dispatchers.Main) {
                        isEditStatusLiveData.value = false
                    }
                }
            }
            if (response is EditResponse) isEditStatusLiveData.value = true
            isLoadingLiveData.value = false
        }
    }
}