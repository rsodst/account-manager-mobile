package com.modulbank.accountmanager.activity.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.ISignInModel
import com.modulbank.accountmanager.models.SignInModel
import retrofit2.HttpException

abstract class SignInGeneralViewModel<TSignInModel>(signInModel : TSignInModel) : ViewModel() where TSignInModel : ISignInModel
{
    protected var signInModel : TSignInModel = signInModel

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

    open fun validate() : Boolean {

        if (signInModel.email.isNullOrBlank()
                || signInModel.password.isNullOrBlank()
        ) {

            emailValidationError.value = "Email is required"
            passwordValidationError.value = "Password is required"

            return false
        }

        if (!isPasswordValid(signInModel.password!!)) {
            passwordValidationError.value = "Password be more length than 5 symbols"
            return false
        }

        if (!isEmailValid(signInModel.email!!)) {
            emailValidationError.value = "Email must equals by pattern asd@asd.com"
            return false
        }

        return true
    }

    // protected

    protected fun handleHttpException(exception : HttpException){
        val responseError =exception.response().errorBody()?.string() as String
        val message = "${exception.message}, ${responseError}"
        state.value = SignInState(isResponseError = "${message}")
    }

    protected  fun isEmailValid(email: String): Boolean
    {
        return email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    protected  fun isPasswordValid(password: String): Boolean
    {
        return password.length > 5
    }
}