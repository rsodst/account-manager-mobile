package com.modulbank.accountmanager.api

import com.modulbank.accountmanager.models.SignInModel
import com.modulbank.accountmanager.models.UserModel
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface IUserApi {
    @POST("user/signin")
    fun sigin(@Body model : SignInModel) : Single<UserModel>

    @POST("user/signup")
    fun signup(@Query("email") email : String,
               @Query("email") password : String) : Single<UserModel>

    companion object Factory {
        fun create() : IUserApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.0.144:45456/")
                .build()

            return retrofit.create(IUserApi::class.java)
        }
    }
}