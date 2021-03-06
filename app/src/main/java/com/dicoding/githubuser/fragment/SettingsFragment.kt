package com.dicoding.githubuser.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.core.os.ConfigurationCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.dicoding.githubuser.R
import com.dicoding.githubuser.notification.AlarmReceiver

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener, PreferenceManager.OnPreferenceTreeClickListener {

    private lateinit var keyDailyRemider: String
    private lateinit var keyChangeLanguage: String
    private lateinit var dailyReminderPreference : SwitchPreference
    private lateinit var changeLanguagePreference : Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        init()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == keyDailyRemider) {
            dailyReminderPreference.isChecked = sharedPreferences.getBoolean(keyDailyRemider, false)
            if (dailyReminderPreference.isChecked)
                alarmReceiver.setRepeatingAlarm(activity?.baseContext as Context)
            else
                alarmReceiver.cancelAlarm(activity?.baseContext as Context)
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            keyChangeLanguage -> {
                val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
                changeLanguagePreference.summary =
                    getString(R.string.change_language_summary) + " " + "(${currentLocale.language})"
                val languageIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(languageIntent)
            }
        }
        return true
    }

    private fun init() {
        keyDailyRemider = getString(R.string.key_daily_reminder)
        dailyReminderPreference = findPreference<SwitchPreference>(keyDailyRemider) as SwitchPreference

        keyChangeLanguage = getString(R.string.key_change_language)
        changeLanguagePreference = findPreference<Preference>(keyChangeLanguage) as Preference

        alarmReceiver = AlarmReceiver()
    }
}