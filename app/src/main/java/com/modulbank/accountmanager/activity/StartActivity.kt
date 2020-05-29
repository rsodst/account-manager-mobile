package com.modulbank.accountmanager.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.modulbank.accountmanager.activity.signin.SignInActivity
import com.modulbank.accountmanager.activity.signup.SignUpActivity
import com.modulbank.accountmanager.databinding.StartLayoutBinding

class StartActivity : AppCompatActivity()
{
    private lateinit var binding : StartLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = StartLayoutBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(binding.root);

        binding.signinButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            this.startActivity(intent)
        }
    }
}