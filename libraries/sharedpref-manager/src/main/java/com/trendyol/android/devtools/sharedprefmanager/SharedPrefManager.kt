package com.trendyol.android.devtools.sharedprefmanager

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.trendyol.android.devtools.sharedprefmanager.di.sharedPrefManagerModule
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerActivity
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinApplication
import embedded.koin.dsl.module

object SharedPrefManager {

    internal lateinit var koin: Koin

    private val context: Context by lazy { koin.get() }
    private val intent by lazy {
        Intent(context, SharedPrefManagerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun init(application: Application) {
        koin = koinApplication {
            androidContext(application.applicationContext)
            androidLogger(Level.DEBUG)

            modules(
                sharedPrefManagerModule(
                    application = application,
                )
            )
        }.koin
    }

    fun show(sharedPrefName: String) {
        koin.loadModules(
            listOf(
                module {
                    scope<SharedPrefManagerActivity> {
                        scoped { androidContext().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE) }
                    }
                }
            )
        )
        context.startActivity(intent)
    }

    fun show(sharedPreferences: SharedPreferences) {
        koin.loadModules(
            listOf(
                module {
                    scope<SharedPrefManagerActivity> {
                        scoped { sharedPreferences }
                    }
                }
            )
        )
        context.startActivity(intent)
    }
}
