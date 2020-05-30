package com.modulbank.accountmanager.activity.account_editor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.activity.accounts.AccountsActivity
import com.modulbank.accountmanager.activity.accounts.AccountsViewModel
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.activity.profile.ProfileEditorViewModel
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.AccountEditorBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountEditorActivity : AppCompatActivity() {

    private lateinit var binding : AccountEditorBinding
    private val viewModel : AccountEditorViewModel by viewModels()
    @Inject lateinit var userDao : UserDao
    @Inject lateinit var accountApi : IAccountApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AccountEditorBinding.inflate(layoutInflater)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        setContentView(binding.root)

        val adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, listOf("RUB", "USD", "EUR"))

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.accountEditorCurrency.adapter = adapter

        binding.accountEditorLimit.setText("10000")
        viewModel.limit.value = "10000"
        viewModel.currency.value = 1

        binding.accountEditorLoader.visibility = View.GONE
        binding.accountEditorDelete.visibility = View.GONE

        val bundle = intent.extras
        if (bundle != null) {
            val accountId = bundle.getString("getAccount")

            binding.accountEditorCaption.setText("Edit account")
            binding.accountEditorCreate.setText("Save")
            binding.accountEditorDelete.visibility = View.VISIBLE

            binding.accountEditorDelete.setOnClickListener{
                viewModel.deleteAccount(userDao, accountApi, accountId!!)
            }

            viewModel.getAccount(userDao, accountApi, accountId!!)


        }

        viewModel.state.observe(this, Observer {
            if (it.isLoading)
            {
                binding.accountEditorLoader.visibility = View.VISIBLE
            }else{
                binding.accountEditorLoader.visibility = View.GONE
            }

            binding.accountEditorCreate.isEnabled = !it.isLoading
            binding.accountEditorDescription.isEnabled = !it.isLoading
            binding.accountEditorLimit.isEnabled = !it.isLoading
            binding.accountEditorCurrency.isEnabled = !it.isLoading

            if (it.isResponseError != null){
                binding.accountEditorError.setText(it.isResponseError)
            }else{
                binding.accountEditorError.setText("")
            }
        })

        viewModel.accountModel.observe(this, Observer {
            binding.accountEditorDescription.setText(it.accountDetail.description)
            binding.accountEditorCurrency.setSelection(it.accountDetail.currency?.toInt()!!)
            binding.accountEditorLimit.setText(it.accountDetail.limitByOperation)
        })

        binding.accountEditorDescription.afterTextChanged {
            viewModel.description.value = it
        }

        binding.accountEditorLimit.afterTextChanged {
            viewModel.limit.value = it
        }

        binding.accountEditorCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.currency.value = position
            }
        }

        binding.accountEditorCreate.setOnClickListener{
            viewModel.saveAccount(userDao, accountApi)
        }



        viewModel.openAccountActivity.observe(this, Observer {
            val intent = Intent(this, AccountsActivity::class.java)
            this.startActivity(intent)
            finish()
        })
    }
}