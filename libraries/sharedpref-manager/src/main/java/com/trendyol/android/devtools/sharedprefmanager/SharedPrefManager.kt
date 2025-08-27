package com.trendyol.android.devtools.sharedprefmanager

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.di.ContextContainer
import com.trendyol.android.devtools.sharedprefmanager.di.SharedPreferencesProvider
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerActivity

object SharedPrefManager {

    private val intent by lazy {
        Intent(ContextContainer.getContext(), SharedPrefManagerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun init(application: Application) {
        ContextContainer.initialize(application)
    }

    fun show(sharedPrefName: String) {
        ContextContainer.setSharedPreferencesProvider(
            SharedPreferencesProvider.ByName(sharedPrefName)
        )
        ContextContainer.getContext().startActivity(intent)
    }

    fun show(sharedPreferences: SharedPreferences) {
        ContextContainer.setSharedPreferencesProvider(
            SharedPreferencesProvider.ProvidedSharedPreferences(
                sharedPreferences,
            ),
        )
        ContextContainer.getContext().startActivity(intent)
    }
}
