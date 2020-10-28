package com.wahyu.hirejobengineer.dashboard.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahyu.hirejobengineer.dashboard.home.HomeApiService
import com.wahyu.hirejobengineer.dashboard.home.HomeModel
import com.wahyu.hirejobengineer.dashboard.home.HomeResponse
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class SearchViewModel : ViewModel(), CoroutineScope {

    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isSearchLiveData = MutableLiveData<Boolean>()
    val listLiveData = MutableLiveData<List<HomeModel>>()
    val errorMessaggeLiveData = MutableLiveData<String?>()

    private lateinit var service: HomeApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSearchService(service: HomeApiService) {
        this.service = service
    }

    fun callSearchApi(searchId: Int, search: String) {
        launch {
            isLoadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    when (searchId) {
                        0 -> service.getProfileJobSeekerByNameRequest(search)
                        1 -> service.getProfileJobSeekerBySkillRequest(search)
                        2 -> service.getProfileJobSeekerByLocationRequest(search)
                        3 -> service.getProfileJobSeekerByJobTitleRequest(search)
                        4 -> service.getProfileJobSeekerByStatusJobRequest(search)
                        else -> {
                        }
                    }
                } catch (e: Throwable) {
                    withContext(Job() + Dispatchers.Main) {

                        if (e is HttpException) {
                            isSearchLiveData.value = false
                            Log.d("android1", "error code = ${isSearchLiveData.value}")
                            when (searchId) {
                                0 -> errorMessaggeLiveData.value = "There is no job seeker has the name of \"$search\""
                                1 -> errorMessaggeLiveData.value = "There is no job seeker with the skill of \"$search\""
                                2 -> errorMessaggeLiveData.value = "There is no job seeker in the area of \"$search\""
                                3 -> errorMessaggeLiveData.value = "There is no job seeker with the job title of \"$search\""
                                4 -> errorMessaggeLiveData.value = "There is no job seeker with the status job of \"$search\""
                                else -> errorMessaggeLiveData.value = "no description"
                            }
                        } else e.printStackTrace()
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
                listLiveData.value = list

                withContext(Job() + Dispatchers.Main) {
                    isSearchLiveData.value = response.success
                }
            }
            isLoadingLiveData.value = false
        }
    }
}