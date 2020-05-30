package com.modulbank.accountmanager.activity.signin

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.modulbank.accountmanager.api.IProfileApi
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.models.users.SignUpModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class SignUpViewModel : SignInGeneralViewModel<SignUpModel>(SignUpModel())
{
    val passwordConfirmationValidationError = MutableLiveData<String>()
    val openProfileEditorActivity = MutableLiveData<Boolean>()

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

    fun signUp(userApi: IUserApi, profileApi: IProfileApi, userDao : UserDao, context : Context) {
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
                            
                            userDao.Insert(it)
                            
                            openProfileEditorActivity.value = true

                            profileApi.confirmation("Bearer ${it.value}")
                                .subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe(
                                    {
                                        val toast = Toast.makeText(
                                            context,
                                            "DEBUG: Profile confirmation completed", Toast.LENGTH_SHORT
                                        )
                                        toast.show()
                                    },
                                    {
                                        if (it is HttpException) {
                                            handleHttpException(it)
                                        }
                                    })
                        },
                        {
                            if (it is HttpException) {
                                handleHttpException(it)
                            }
                        })


    }

}