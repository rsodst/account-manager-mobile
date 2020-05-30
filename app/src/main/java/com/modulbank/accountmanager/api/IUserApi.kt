package com.modulbank.accountmanager.api

import com.modulbank.accountmanager.EnvironmentSettings
import com.modulbank.accountmanager.models.users.SignInModel
import com.modulbank.accountmanager.models.users.SignUpModel
import com.modulbank.accountmanager.models.users.UserModel
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface IUserApi {
    @POST("user/signin")
    fun sigin(@Body model : SignInModel) : Single<UserModel>

    @POST("user/signup")
    fun sigup(@Body model : SignUpModel) : Single<UserModel>

    companion object Factory {
        fun create() : IUserApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(EnvironmentSettings.apiUrl)
                .build()

            return retrofit.create(IUserApi::class.java)
        }
    }
}