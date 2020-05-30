package com.modulbank.accountmanager.activity.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.activity.profile.ProfileEditorActivity
import com.modulbank.accountmanager.activity.signin.SignInActivity
import com.modulbank.accountmanager.activity.signin.SignUpViewModel
import com.modulbank.accountmanager.api.IProfileApi
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.AppModule
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.SigninActivityBinding
import com.modulbank.accountmanager.databinding.SignupActivityBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject


class SignUpActivity : AppCompatActivity()
{

    private val model: SignUpViewModel by viewModels()
    private lateinit var binding : SignupActivityBinding
    @Inject lateinit var userApi : IUserApi
    @Inject lateinit var profileApi : IProfileApi
    @Inject lateinit var userDao : UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        binding = SignupActivityBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)

        binding.signupLoad.visibility = View.GONE

        model.openProfileEditorActivity.observe(this, Observer {
            val intent = Intent(this, ProfileEditorActivity::class.java)
            intent.putExtra("showSkipButton", true)
            this.startActivity(intent)
            finish()
        })

        model.state.observe(this, Observer {
            binding.signupEmail.isEnabled = !it.isLoading
            binding.signupPassword.isEnabled = !it.isLoading
            binding.signup.isEnabled = !it.isLoading

            if (it.isLoading){
                binding.signupLoad.visibility = View.VISIBLE
            }else{
                binding.signupLoad.visibility = View.GONE
            }

            if (it.isResponseError != null){
                binding.signupErrors.setText(it.isResponseError)
            }else{
                binding.signupErrors.setText("")
            }
        })

        model.passwordValidationError.observe(this, Observer {
            binding.signupPassword.error = it
        })

        model.passwordConfirmationValidationError.observe(this, Observer {
            binding.signupPasswordConfirmation.error = it
        })

        model.emailValidationError.observe(this, Observer {
            binding.signupEmail.error = it
        })

        binding.signupEmail.afterTextChanged {
            model.changeEmail(binding.signupEmail.text.toString())
        }

        binding.signupPassword.afterTextChanged {
            model.changePassword(binding.signupPassword.text.toString())
        }

        binding.signupPasswordConfirmation.afterTextChanged {
            model.changePasswordConfirmation(binding.signupPasswordConfirmation.text.toString())
        }

        binding.signup.setOnClickListener({
            model.signUp(userApi, profileApi, userDao, this)
        })
    }
}