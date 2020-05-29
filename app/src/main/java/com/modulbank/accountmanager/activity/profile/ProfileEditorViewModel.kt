package com.modulbank.accountmanager.activity.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.api.IProfileApi
import com.modulbank.accountmanager.models.profile.ProfileModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ProfileEditorViewModel : ViewModel()
{
    val firstNameValidation = MutableLiveData<String>()
    val middleNameValidation = MutableLiveData<String>()

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val middleName = MutableLiveData<String>()

    val profileModel = MutableLiveData<ProfileModel>()

    val state = MutableLiveData<ProfileState>()

    val openAccountActivity = MutableLiveData<Boolean>()

    fun tryGetPersonDetails(profileApi : IProfileApi, userDao: UserDao){

        val user = userDao.Get() ?: return

        state.value = ProfileState(isLoading = true)

        profileApi.get("Bearer ${user.value}")
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    profileModel.value = it
                    state.value = ProfileState(isLoading = false)
                },
                {
                    if (it is HttpException) {
                        handleHttpException(it)
                    }
                    else{
                        state.value = ProfileState(isLoading = false)
                    }
                })
    }


    fun save(profileApi : IProfileApi, userDao: UserDao)
    {
        val user = userDao.Get() ?: return

        if (firstName.value.isNullOrEmpty())
        {
            firstNameValidation.value = "FirstName required"
            return
        }

        if (middleName.value.isNullOrEmpty())
        {
            middleNameValidation.value = "MiddleName required"
            return
        }

        state.value = ProfileState(isLoading = true)

        val model =  ProfileModel(
        firstName = firstName.value,
        lastName = lastName.value,
        middleName = middleName.value
        )

        if (profileModel.value == null) {
            profileApi.create(
                "Bearer ${user.value}",
                model
            )
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    {
                       openAccountActivity.value = true
                    },
                    {
                        if (it is HttpException) {
                            handleHttpException(it)
                        }
                        else
                        {
                            state.value = ProfileState(isLoading = false)
                        }
                        state.value = ProfileState(isLoading = false)
                    })
        }else{
            profileApi.update(
                "Bearer ${user.value}",
                model)
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    {
                        openAccountActivity.value = true
                    },
                    {
                        if (it is HttpException) {
                            handleHttpException(it)
                        }else
                        {
                            state.value = ProfileState(isLoading = false)
                        }
                    })
        }
    }

    // private

    private fun handleHttpException(exception : HttpException){
        val responseError =exception.response().errorBody()?.string() as String
        val message = "${exception.message}, ${responseError}"
        state.value = ProfileState(isResponseError = "${message}")
    }

}