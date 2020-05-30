package com.modulbank.accountmanager.activity.account_editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.models.accounts.AccountEditModel
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.users.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class AccountEditorViewModel : ViewModel() {
    val state = MutableLiveData<AccountEditorState>()

    val description = MutableLiveData<String>()
    val currency = MutableLiveData<Int>()
    val limit = MutableLiveData<String>()

    val limitError =MutableLiveData<String>()

    val accountModel = MutableLiveData<AccountModel>()

    val openAccountActivity = MutableLiveData<Boolean>()

    fun getAccount(userDao: UserDao, accountApi : IAccountApi, id : String){
        val user = userDao.Get() ?: return

        state.value = AccountEditorState(isLoading = true)

        accountApi.getAccount("Bearer ${user.value}", id)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    accountModel.value = it

                    state.value = AccountEditorState(isLoading = false)
                },
                {
                    if (it is HttpException)
                    {
                        handleHttpException(it)
                    }
                    else
                    {
                        state.value = AccountEditorState(isLoading = false)
                    }
                })
    }

    fun saveAccount(userDao: UserDao, accountApi : IAccountApi){

        if (limit.value == null || limit.value.isNullOrEmpty()){
            limitError.value = "Account limit is required"
            return
        }

        val user = userDao.Get() ?: return

        state.value = AccountEditorState(isLoading = true)

        val model=AccountEditModel(
            description = description.value,
            limitByOperation = limit.value?.toBigDecimal(),
            currency = currency.value
        )

        if (accountModel.value == null) {
            accountApi.createAccount("Bearer ${user.value}", model)
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    {
                        state.value = AccountEditorState(isLoading = false)
                        openAccountActivity.value = true
                    },
                    {
                        if (it is HttpException) {
                            handleHttpException(it)
                        } else {
                            state.value = AccountEditorState(isLoading = false)
                        }
                    })
        }else{
            accountApi.updateAccount("Bearer ${user.value}", model, accountModel?.value?.id!!)
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    {
                        state.value = AccountEditorState(isLoading = false)
                        openAccountActivity.value = true
                    },
                    {
                        if (it is HttpException)
                        {
                            handleHttpException(it)
                        }
                        else
                        {
                            state.value = AccountEditorState(isLoading = false)
                        }
                    })
        }
    }

    fun deleteAccount(userDao : UserDao, accountApi : IAccountApi, id : String) {
        val user = userDao.Get() ?: return

        state.value = AccountEditorState(isLoading = true)

        accountApi.deleteAccount("Bearer ${user.value}", id)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    state.value = AccountEditorState(isLoading = false)
                    openAccountActivity.value = true
                },
                {
                    if (it is HttpException)
                    {
                        handleHttpException(it)
                    }
                    else
                    {
                        state.value = AccountEditorState(isLoading = false)
                    }
                })
    }


    // private
    private fun handleHttpException(exception : HttpException){
        val responseError =exception.response().errorBody()?.string() as String
        val message = "${exception.message}, ${responseError}"
        state.value = AccountEditorState(isResponseError = "${message}")
    }
}