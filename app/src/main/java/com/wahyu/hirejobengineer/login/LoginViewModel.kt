package com.wahyu.hirejobengineer.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonParser
import com.wahyu.hirejobengineer.util.Key
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {

    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoginLiveData = MutableLiveData<Boolean>()
    val errorMessageLiveData = MutableLiveData<String>()

    private lateinit var service: LoginApiService
    private lateinit var sharedPref: SharedPreferencesUtil

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setLoginService(service: LoginApiService) {
        this.service = service
    }

    fun setSharedPref(sharePref: SharedPreferencesUtil) {
        this.sharedPref = sharePref
    }

    fun callSignInApi(email: String, password: String) {
        launch {
            isLoadingLiveData.value = true
            @Suppress("DEPRECATION") val response = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            val message: String
                            val errorJsonString = e.response()?.errorBody()?.string()
                            message = JsonParser().parse(errorJsonString).asJsonObject["message"].asString
                            withContext(Job() + Dispatchers.Main) {
                                errorMessageLiveData.value = message
                                isLoginLiveData.value = false
                            }
                        }
                        else -> e.printStackTrace()
                    }
                }
            }
            if (response is LoginResponse) {
                sharedPref.put(Key.PREF_TOKEN, response.data?.token!!)
                sharedPref.put(Key.PREF_USER_ID, response.data.id!!)
                sharedPref.put(Key.PREF_IS_LOGIN, true)
                isLoginLiveData.value = true
            }
            isLoadingLiveData.value = false
        }
    }
}