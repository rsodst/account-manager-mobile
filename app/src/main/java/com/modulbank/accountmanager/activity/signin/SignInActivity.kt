package com.modulbank.accountmanager.activity.signin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.modulbank.accountmanager.R
import com.modulbank.accountmanager.activity.extensions.afterTextChanged
import com.modulbank.accountmanager.api.IUserApi
import com.modulbank.accountmanager.dagger.components.DaggerApiComponent
import javax.inject.Inject


class SignInActivity : AppCompatActivity()
{

    private val model: SignInViewModel by viewModels()
    @Inject lateinit var userApi : IUserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerApiComponent.create().inject(this)

        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.signin_activity)

        val email = findViewById<EditText>(R.id.signin_email)
        val password = findViewById<EditText>(R.id.signin_password)
        val signInButton =findViewById<Button>(R.id.signin)
        val errorMessages = findViewById<TextView>(R.id.signin_errors)

        val progressBar = findViewById<ProgressBar>(R.id.signin_load)

        progressBar.visibility = View.GONE

        model.state.observe(this, Observer {
            email.isEnabled = !it.isLoading
            password.isEnabled = !it.isLoading
            signInButton.isEnabled = !it.isLoading

            if (it.isLoading){
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }

            if (it.isResponseError != null){
                errorMessages.setText(it.isResponseError)
            }
        })

        model.passwordValidationError.observe(this, Observer {
            password.error = it
        })

        model.emailValidationError.observe(this, Observer {
            email.error = it
        })

        email.afterTextChanged {
            model.changeEmail(email.text.toString())
        }

        password.afterTextChanged {
            model.changePassword(password.text.toString())
        }

        signInButton.setOnClickListener({
            model.login(userApi)
        })


    }
}