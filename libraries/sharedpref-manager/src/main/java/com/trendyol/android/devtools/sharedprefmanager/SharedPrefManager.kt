package com.trendyol.android.devtools.sharedprefmanager

import android.app.Application
import android.content.Intent
import com.trendyol.android.devtools.sharedprefmanager.di.ContextContainer
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
        ContextContainer.setSharedPrefName(sharedPrefName)
        ContextContainer.getContext().startActivity(intent)
    }
}
