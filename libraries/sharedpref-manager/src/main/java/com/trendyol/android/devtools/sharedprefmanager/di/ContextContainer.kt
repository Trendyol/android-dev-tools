package com.trendyol.android.devtools.sharedprefmanager.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefUpdateTypeValidator

internal object ContextContainer {

    val mainContainer by lazy {
        MainContainer(
            sharedPrefUseCaseContainer = sharedPrefUseCaseContainer,
            sharedPrefUpdateTypeValidator = sharedPrefUpdateTypeValidator,
        )
    }

    private val sharedPrefUseCaseContainer by lazy {
        SharedPrefUseCaseContainer(application.applicationContext, sharedPreferencesProvider)
    }
    private val sharedPrefUpdateTypeValidator by lazy {
        SharedPrefUpdateTypeValidator()
    }
    private lateinit var application: Application
    private lateinit var sharedPreferencesProvider: SharedPreferencesProvider
    fun getContext(): Context =
        if (::application.isInitialized) {
            application.applicationContext
        } else {
            throw NullPointerException("SharedPref Manager library is not initialized.")
        }

    fun initialize(application: Application) {
        this.application = application
    }

    fun setSharedPreferencesProvider(provider: SharedPreferencesProvider) {
        this.sharedPreferencesProvider = provider
    }
}

sealed class SharedPreferencesProvider {
    abstract fun provide(context: Context): SharedPreferences
    class ByName(val name: String): SharedPreferencesProvider() {
        override fun provide(context: Context): SharedPreferences {
            return context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
    }

    class ProvidedSharedPreferences(val sharedPreferences: SharedPreferences): SharedPreferencesProvider() {
        override fun provide(context: Context): SharedPreferences {
            return sharedPreferences
        }
    }

}
