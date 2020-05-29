package com.modulbank.accountmanager.activity.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.SignInModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.Result.response


class SignInViewModel : ViewModel() {

    private val signInModel = SignInModel()

    val emailValidationError = MutableLiveData<String>()
    val passwordValidationError = MutableLiveData<String>()

    val state = MutableLiveData<SignInState>()

    fun changeEmail(email: String){
        if (!isEmailValid(email)){
            emailValidationError.value = "Email must equals by pattern asd@asd.com"
        }
        signInModel.email = email

    }

    fun changePassword(password: String){
        if (!isPasswordValid(password)) {
            passwordValidationError.value = "Password be more length than 5 symbols"
        }

        signInModel.password = password
    }

    fun login(userApi : IUserApi){

        if (signInModel.email.isNullOrBlank()
            || signInModel.password.isNullOrBlank()){

            emailValidationError.value = "Email is required"
            passwordValidationError.value = "Password is required"

            return
        }

        if (!isPasswordValid(signInModel.password!!)) {
            passwordValidationError.value = "Password be more length than 5 symbols"
            return
        }

        if (!isEmailValid(signInModel.email!!)) {
            emailValidationError.value = "Email must equals by pattern asd@asd.com"
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
                        val responseError =it.response().errorBody()?.string() as String
                        val message = "${it.message}, ${responseError}"
                        state.value = SignInState(isResponseError = "${message}")
                    }
                })

    }

    // private

    private fun isEmailValid(email: String): Boolean
    {
        return email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean
    {
        return password.length > 5
    }
}