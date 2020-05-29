package com.modulbank.accountmanager.activity.signin

import androidx.lifecycle.MutableLiveData
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.users.SignInModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class SignInViewModel : SignInGeneralViewModel<SignInModel>(SignInModel()) {

    val openAccountActivity = MutableLiveData<Boolean>()

    fun signIn(userApi: IUserApi, userDao : UserDao) {
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

                userDao.Insert(it)

                openAccountActivity.value = true
            },
            {
                if (it is HttpException) {
                    handleHttpException(it)
                }
            })
    }
}

