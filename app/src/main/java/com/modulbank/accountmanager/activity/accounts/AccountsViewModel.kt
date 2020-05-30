package com.modulbank.accountmanager.activity.accounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.activity.signin.SignInState
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.Exception

class AccountsViewModel : ViewModel() {
    val accounts = MutableLiveData<List<AccountModel>>()

    val state = MutableLiveData<AccountsState>()

    fun LoadAccountList(userDao : UserDao, accountApi  :IAccountApi) {
        val user = userDao.Get() ?: return

        state.value = AccountsState(isLoading = true)

        accountApi.get("Bearer ${user.value}")
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    accounts.value = it
                    state.value = AccountsState(isLoading = false)
                },
                {
                    if (it is HttpException) {
                        handleHttpException(it)
                    }

                    state.value = AccountsState(isResponseError = "${it.message}")
                })
    }

    // private

    protected fun handleHttpException(exception : HttpException){
        val responseError =exception.response().errorBody()?.string() as String
        val message = "${exception.message}, ${responseError}"
        state.value = AccountsState(isResponseError = "${message}")
    }
}