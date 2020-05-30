package com.modulbank.accountmanager.api

import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.profile.ProfileModel
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IAccountApi {
    @GET("accounts/list/0/100")
    fun get(@Header("Authorization") token : String) : Single<List<AccountModel>>

    companion object Factory {
        fun create() : IAccountApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.0.144:45459/")
                .build()

            return retrofit.create(IAccountApi::class.java)
        }
    }
}