package com.modulbank.accountmanager.api

import com.modulbank.accountmanager.EnvironmentSettings
import com.modulbank.accountmanager.models.profile.ProfileModel
import com.modulbank.accountmanager.models.users.SignInModel
import com.modulbank.accountmanager.models.users.SignUpModel
import com.modulbank.accountmanager.models.users.UserModel
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface IProfileApi {
    @POST("profile/person-details")
    fun create(
        @Header("Authorization") token : String,
        @Body model : ProfileModel
    ) : Single<ProfileModel>

    @GET("profile/person-details")
    fun get(@Header("Authorization") token : String) : Single<ProfileModel>

    @PUT("profile/person-details")
    fun update(
        @Header("Authorization") token : String,
        @Body model : ProfileModel
    ) : Single<ProfileModel>

    @POST("debug/profile-confirmation")
    fun confirmation(
        @Header("Authorization") token : String
    ) : Maybe<ResponseBody>

    companion object Factory {
        fun create() : IProfileApi {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(EnvironmentSettings.apiUrl)
                .build()

            return retrofit.create(IProfileApi::class.java)
        }
    }
}