package com.modulbank.accountmanager.activity.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.activity.accounts.AccountsActivity
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.api.IProfileApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.ProfileEditorActivityBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class ProfileEditorActivity : AppCompatActivity(){

    private lateinit var binding : ProfileEditorActivityBinding
    private val viewModel : ProfileEditorViewModel by viewModels()

    @Inject lateinit var profileApi : IProfileApi
    @Inject lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        supportActionBar?.hide()
        binding = ProfileEditorActivityBinding.inflate(layoutInflater)

        binding.profileEditorLoader.visibility = View.GONE

        val showSkipButton = intent.extras?.getBoolean("showSkipButton")

        if (showSkipButton == null || !showSkipButton) {
            binding.profileSkip.visibility = View.GONE
        }

        setContentView(binding.root)

        viewModel.state.observe(this, Observer {
            if (it.isLoading)
            {
                binding.profileEditorLoader.visibility = View.VISIBLE
            }else{
                binding.profileEditorLoader.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.profileEdtiorError.setText(it.isResponseError)
            }else{
                binding.profileEdtiorError.setText("")
            }
        })

        viewModel.profileModel.observe(this, Observer {
            binding.profileFirstname.setText(it.firstName)
            binding.profileLastname.setText(it.lastName)
            binding.profileMiddlename.setText(it.middleName)
        })

        binding.profileMiddlename.afterTextChanged {
            viewModel.middleName.value = it
        }

        binding.profileFirstname.afterTextChanged {
            viewModel.firstName.value = it
        }

        binding.profileLastname.afterTextChanged {
            viewModel.lastName.value = it
        }

        viewModel.firstNameValidation.observe(this, Observer {
            if (it.isNullOrEmpty()){
                binding.profileFirstname.error = it
            }else{
                binding.profileFirstname.error = ""
            }
        })

        viewModel.middleNameValidation.observe(this, Observer {
            if (it.isNullOrEmpty()){
                binding.profileMiddlename.error = it
            }else{
                binding.profileMiddlename.error = ""
            }
        })

        binding.profileSave.setOnClickListener {
            viewModel.save(profileApi, userDao)
        }

        viewModel.openAccountActivity.observe(this, Observer {
            val intent = Intent(this, AccountsActivity::class.java)
            this.startActivity(intent)
            finish()
        })

        binding.profileSkip.setOnClickListener {
            val intent = Intent(this, AccountsActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        viewModel.tryGetPersonDetails(profileApi, userDao)
    }
}