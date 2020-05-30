package com.modulbank.accountmanager.activity.accoun_operation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.AccountOperationLayoutBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountOperationActivity : AppCompatActivity() {

    @Inject lateinit var userDao : UserDao
    @Inject lateinit var accountApi : IAccountApi
    private lateinit var binding : AccountOperationLayoutBinding
    val viewModel : AccountOperationViewModel by viewModels<AccountOperationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AccountOperationLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding.accountOperationLoader.visibility = View.GONE

        binding.accountOperationRecycler.apply{
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = AccountOperationListAdapter(listOf())
        }

        viewModel.actions.observe(this, Observer {
            binding.accountOperationRecycler.apply{
                hasFixedSize()
                layoutManager = LinearLayoutManager(context)
                adapter = AccountOperationListAdapter(it)
            }
        })

        viewModel.state.observe(this, Observer {
            if (it.isLoading){
                binding.accountOperationLoader.visibility = View.VISIBLE
            }else{
                binding.accountOperationLoader.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.accountOperationErrors.setText(it.isResponseError)
            }else{
                binding.accountOperationErrors.setText("")
            }
        })

        val bundle = intent.extras
        val accountId = bundle?.getString("accountId")

        viewModel.LoadAccountActionsList(userDao, accountApi, accountId!!)
    }
}