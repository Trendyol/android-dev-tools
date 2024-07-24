package com.trendyol.android.devtools.sharedprefmanager.di

import android.app.Application
import android.content.Context
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefUpdateTypeValidator

internal object ContextContainer {

    val mainContainer by lazy {
        MainContainer(
            sharedPrefUseCaseContainer = sharedPrefUseCaseContainer,
            sharedPrefUpdateTypeValidator = sharedPrefUpdateTypeValidator,
        )
    }

    private val sharedPrefUseCaseContainer by lazy {
        SharedPrefUseCaseContainer(application.applicationContext, sharedPrefName)
    }
    private val sharedPrefUpdateTypeValidator by lazy {
        SharedPrefUpdateTypeValidator()
    }
    private lateinit var application: Application
    private lateinit var sharedPrefName: String

    fun getContext(): Context =
        if (::application.isInitialized) {
            application.applicationContext
        } else {
            throw NullPointerException("SharedPref Manager library is not initialized.")
        }

    fun initialize(application: Application) {
        this.application = application
    }

    fun setSharedPrefName(sharedPrefName: String) {
        this.sharedPrefName = sharedPrefName
    }
}
