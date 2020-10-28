package com.wahyu.hirejobengineer.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonParser
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class RegisterViewModel : ViewModel(), CoroutineScope {

    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isRegisterLiveData = MutableLiveData<Boolean>()
    val errorMessageLiveData = MutableLiveData<String>()

    private lateinit var service: RegisterApiService
    private lateinit var sharedPref: SharedPreferencesUtil

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setRegisterService(service: RegisterApiService) {
        this.service = service
    }

    fun setSharedPref(sharePref: SharedPreferencesUtil) {
        this.sharedPref = sharePref
    }

    fun callSignUpApi(
        name: String,
        email: String,
        password: String,
        PhoneNumber: String
    ) {
        launch {
            isLoadingLiveData.value = true
            @Suppress("DEPRECATION") val response = withContext(Dispatchers.IO) {
                try {
                    service.registerRequest(name, email, password, PhoneNumber)
                } catch (e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            val message: String
                            val errorJsonString = e.response()?.errorBody()?.string()
                            message =
                                JsonParser().parse(errorJsonString).asJsonObject["message"].asString
                            withContext(Job() + Dispatchers.Main) {
                                errorMessageLiveData.value = message
                                isRegisterLiveData.value = false
                            }
                        }
                        else -> e.printStackTrace()
                    }
                }
            }
            if (response is RegisterResponse) {
                isRegisterLiveData.value = true
                try {
                    service.createProfileJobSeeker(response.data?.userId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            isLoadingLiveData.value = false
        }
    }
}