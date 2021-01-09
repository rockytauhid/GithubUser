package com.dicoding.githubuser.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuser.R
import com.dicoding.githubuser.fragment.SettingsFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, SettingsFragment()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        SettingsFragment().setDailyAlarm()
        onBackPressed()
        return true
    }
}