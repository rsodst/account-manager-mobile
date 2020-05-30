package com.modulbank.accountmanager.activity.accoun_operation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.activity.signin.SignInState
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.models.accounts.AccountActionModel
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.lang.Exception

class AccountOperationViewModel : ViewModel() {
    val actions = MutableLiveData<List<AccountActionModel>>()

    val state = MutableLiveData<AccountOperationState>()

    fun LoadAccountActionsList(userDao : UserDao, accountApi  :IAccountApi, accountId : String) {
        val user = userDao.Get() ?: return

        state.value = AccountOperationState(isLoading = true)

        accountApi.getActions("Bearer ${user.value}", accountId)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    actions.value = it
                    state.value = AccountOperationState(isLoading = false)
                },
                {
                    if (it is HttpException) {
                        handleHttpException(it)
                    }

                    state.value = AccountOperationState(isResponseError = "${it.message}")
                })
    }

    // private

    protected fun handleHttpException(exception : HttpException){
        val responseError =exception.response().errorBody()?.string() as String
        val message = "${exception.message}, ${responseError}"
        state.value = AccountOperationState(isResponseError = "${message}")
    }
}