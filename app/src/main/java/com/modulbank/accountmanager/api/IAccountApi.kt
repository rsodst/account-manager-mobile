package com.modulbank.accountmanager.api

import com.modulbank.accountmanager.EnvironmentSettings
import com.modulbank.accountmanager.models.accounts.*
import com.modulbank.accountmanager.models.profile.ProfileModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Call
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.reactivestreams.Subscriber
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// TOOD: pagination skip/take

interface IAccountApi {
    @GET("accounts/list/0/100")
    fun get(@Header("Authorization") token : String) : Single<List<AccountModel>>

    @GET("accounts/{accountId}/actions/0/100")
    fun getActions(@Header("Authorization") token : String, @Path("accountId") accountId : String) : Single<List<AccountActionModel>>

    @GET("accounts/{accountId}")
    fun getAccount(@Header("Authorization") token : String, @Path("accountId") accountId : String) : Single<AccountModel>

    @DELETE("accounts/{accountId}")
    fun deleteAccount(@Header("Authorization") token : String, @Path("accountId") accountId : String) : Single<AccountModel>

    @POST("accounts/create")
    fun createAccount(@Header("Authorization") token : String, @Body accountModel : AccountEditModel) : Single<AccountModel>

    @POST("accounts/{accountId}/transfer")
    fun transfer(@Header("Authorization") token : String,@Path("accountId") accountId : String, @Body model : TransferModel) : Single<RequestBody>

    @POST("accounts/{accountId}/refill")
    fun refill(@Header("Authorization") token : String, @Path("accountId") accountId : String, @Body model : RefillModel) : Maybe<Call>

    @PUT("accounts/{accountId}")
    fun updateAccount(@Header("Authorization") token : String, @Body accountModel : AccountEditModel, @Path("accountId") accountId : String) : Single<AccountModel>

    companion object Factory {
        fun create() : IAccountApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(EnvironmentSettings.apiUrl)
                .build()

            return retrofit.create(IAccountApi::class.java)
        }
    }
}