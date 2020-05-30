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
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.activity.settings.SettingsActivity
import com.modulbank.accountmanager.api.IAccountApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.PrivateLayoutBinding
import com.modulbank.accountmanager.databinding.ProfileEditorActivityBinding
import com.modulbank.accountmanager.models.accounts.AccountModel
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class AccountsActivity : AppCompatActivity() {

    @Inject lateinit var userDao : UserDao
    @Inject lateinit var accountApi : IAccountApi
    private lateinit var binding : PrivateLayoutBinding
    val viewModel : AccountsViewModel by viewModels<AccountsViewModel>()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottombar_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PrivateLayoutBinding.inflate(layoutInflater)

        setSupportActionBar(binding.bottomAppBar)

        setContentView(binding.root)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding.privateLayoutLoader.visibility = View.GONE

        binding.recycler.apply{
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = AccountListAdapter(listOf())
        }

        viewModel.accounts.observe(this, Observer {
            binding.recycler.apply{
                hasFixedSize()
                layoutManager = LinearLayoutManager(context)
                adapter = AccountListAdapter(it)
            }
        })

        viewModel.state.observe(this, Observer {
            if (it.isLoading){
                binding.privateLayoutLoader.visibility = View.VISIBLE
            }else{
                binding.privateLayoutLoader.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.privateLayoutErrors.setText(it.isResponseError)
            }else{
                binding.privateLayoutErrors.setText("")
            }
        })


        viewModel.LoadAccountList(userDao, accountApi)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.appbar_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                this.startActivity(intent)
            }
            R.id.appbar_profile_editor -> {
                val intent = Intent(this, ProfileEditorActivity::class.java)
                this.startActivity(intent)
            }
        }

        return true
    }

    override fun onBackPressed() {

    }
}