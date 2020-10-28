package com.wahyu.hirejobengineer.dashboard.home.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahyu.hirejobengineer.dashboard.home.HomeApiService
import com.wahyu.hirejobengineer.dashboard.home.HomeModel
import com.wahyu.hirejobengineer.dashboard.home.HomeResponse
import com.wahyu.hirejobengineer.dashboard.home.detail.portofolio.PortofolioModel
import com.wahyu.hirejobengineer.dashboard.home.detail.portofolio.PortofolioResponse
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailHomeViewModel : ViewModel(), CoroutineScope {

    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isDetailProfileLiveData = MutableLiveData<Boolean>()
    val isPortofolioLiveData = MutableLiveData<Boolean>()
    val detailProfileLiveData = MutableLiveData<List<HomeModel>>()
    val portofolioLiveData = MutableLiveData<List<PortofolioModel>>()

    private lateinit var service: HomeApiService
    private lateinit var sharedPref: SharedPreferencesUtil

    override val coroutineContext: CoroutineContext get() = Job() + Dispatchers.Main

    fun setDetailProfileService(service: HomeApiService) {
        this.service = service
    }

    fun setSharedPref(sharePref: SharedPreferencesUtil) {
        this.sharedPref = sharePref
    }

    fun callDetailProfileApi(id: Int) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getProfileJobSeekerByIdRequest(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isDetailProfileLiveData.value = false
                    }
                }
            }
            if (response is HomeResponse) {
                val list = response.data?.map {
                    HomeModel(
                        it.id, it.userId, it.idPortofolio, it.idSkill, it.email, it.name, it.jobTitle, it.statusJob,
                        it.address, it.city, it.workplace, it.image, it.description, it.createdAt, it.updatedAt
                    )
                } ?: listOf()
                detailProfileLiveData.value = list
                isDetailProfileLiveData.value = true
            }
            isLoadingLiveData.value = false
        }
    }

    fun callPortofolioApi(id: Int) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getPortofolioJobSeekerByIdRequest(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isPortofolioLiveData.value = false
                    }
                }
            }
            if (response is PortofolioResponse) {
                val list = response.data?.map {
                    PortofolioModel(
                        it.idPortofolio, it.idJobSeeker, it.appName, it.description, it.publishLink, it.repoLink, it.workplace,
                        it.type, it.portoImage
                    )
                } ?: listOf()
                portofolioLiveData.value = list
                isPortofolioLiveData.value = true
            }
            isLoadingLiveData.value = false
        }
    }
}