package com.modulbank.accountmanager.activity.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.modulbank.accountmanager.activity.StartActivity
import com.modulbank.accountmanager.dagger.components.DaggerAppComponent
import com.modulbank.accountmanager.dagger.modules.DatabaseModule
import com.modulbank.accountmanager.databinding.SettingsActivityBinding
import com.modulbank.accountmanager.models.users.UserDao
import javax.inject.Inject

class SettingsActivity : AppCompatActivity(){

    private lateinit var binding : SettingsActivityBinding
    @Inject lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)

        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(application))
            .build().inject(this)

        setContentView(binding.root)

        binding.settingsExitButton.setOnClickListener{
            userDao.Delete()
            val intent = Intent(this, StartActivity::class.java)
            this.startActivity(intent)
        }
    }

}