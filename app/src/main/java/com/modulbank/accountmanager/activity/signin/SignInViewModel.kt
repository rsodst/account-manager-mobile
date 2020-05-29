package com.modulbank.accountmanager.activity.signin

import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.SignInModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class SignInViewModel : SignInGeneralViewModel<SignInModel>(SignInModel()) {

    fun signIn(userApi: IUserApi) {
        if (!super.validate()){
            return
        }

        state.value = SignInState(isLoading = true)

        userApi.sigin(signInModel)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
            {
                state.value = SignInState(isLoading = false, isResponseError = null)
                val t = it
            },
            {
                if (it is HttpException) {
                    handleHttpException(it)
                }
            })
    }
}

