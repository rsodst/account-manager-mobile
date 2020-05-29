package com.modulbank.accountmanager.activity.signin

import androidx.lifecycle.MutableLiveData
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.SignUpModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class SignUpViewModel : SignInGeneralViewModel<SignUpModel>(SignUpModel())
{
    val passwordConfirmationValidationError = MutableLiveData<String>()

    fun changePasswordConfirmation(password: String){
        if (!isPasswordValid(password)) {
            passwordConfirmationValidationError.value = "Password be more length than 5 symbols"
        }

        if (password != signInModel.password){
            passwordConfirmationValidationError.value = "Password mismatch"
        }

        signInModel.paswordConfirmation = password
    }

    override fun validate() : Boolean {
        super.validate()

        if (signInModel.paswordConfirmation.isNullOrBlank()){
            passwordConfirmationValidationError.value = "Password confirmation required"
            return false
        }

        if (signInModel.password != signInModel.paswordConfirmation){
            passwordConfirmationValidationError.value = "Password mismatch"
            return false
        }

        return true
    }

    fun signUp(userApi: IUserApi) {
        if (!validate()){
            return
        }

        state.value = SignInState(isLoading = true)

        userApi.sigup(signInModel)
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