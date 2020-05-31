package com.modulbank.accountmanager.activity.accounts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.account_editor.AccountEditorActivity
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.activity.settings.SettingsActivity
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.AccountLayoutBinding
import com.modulbank.accountmanager.databinding.ProfileEditorActivityBinding
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountsActivity : AppCompatActivity() {

    @Inject lateinit var userDao : UserDao
    @Inject lateinit var accountApi : IAccountApi
    private lateinit var binding : AccountLayoutBinding
    val viewModel : AccountsViewModel by viewModels<AccountsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AccountLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding.accountLayoutLoader.visibility = View.GONE
        binding.accountLayoutEmptyCaption.visibility = View.VISIBLE

        binding.accountLayoutRecycler.apply{
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = AccountListAdapter(listOf())
        }

        viewModel.accounts.observe(this, Observer {

            if (it.isNotEmpty()){
                binding.accountLayoutEmptyCaption.visibility = View.GONE
            }

            binding.accountLayoutRecycler.apply{
                hasFixedSize()
                layoutManager = LinearLayoutManager(context)
                adapter = AccountListAdapter(it)
            }
        })

        viewModel.state.observe(this, Observer {
            if (it.isLoading){
                binding.accountLayoutLoader.visibility = View.VISIBLE
            }else{
                binding.accountLayoutLoader.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.accountLayoutErrors.setText(it.isResponseError)
            }else{
                binding.accountLayoutErrors.setText("")
            }
        })

        binding.accountAdd.setOnClickListener{
            val intent = Intent(this, AccountEditorActivity::class.java)
            this.startActivity(intent)
        }

        binding.appSettings.setOnClickListener({
            val intent = Intent(this, SettingsActivity::class.java)
            this.startActivity(intent)
        })

        binding.appProfile.setOnClickListener({
            val intent = Intent(this, ProfileEditorActivity::class.java)
            this.startActivity(intent)
        })

        viewModel.LoadAccountList(userDao, accountApi)
    }

    override fun onBackPressed() {

    }
}