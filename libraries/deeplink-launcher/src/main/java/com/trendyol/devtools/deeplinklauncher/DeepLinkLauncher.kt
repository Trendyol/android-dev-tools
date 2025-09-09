package com.trendyol.devtools.deeplinklauncher

import android.app.Application
import android.content.Context
import android.content.Intent
import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import com.trendyol.devtools.deeplinklauncher.internal.di.deepLinkLauncherModule
import com.trendyol.devtools.deeplinklauncher.internal.domain.AppDeepLinkUseCase
import com.trendyol.devtools.deeplinklauncher.internal.ui.MainActivity
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.android.ext.koin.androidLogger
import embedded.koin.core.Koin
import embedded.koin.core.logger.Level
import embedded.koin.dsl.koinApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.squareup.moshi.Moshi

object DeepLinkLauncher {

    internal lateinit var koin: Koin

    private val superVisorJob = SupervisorJob()
    private val scope = CoroutineScope(superVisorJob + Dispatchers.IO)

    fun init(
        application: Application
    ) {
        koin = koinApplication {
            androidContext(application.applicationContext)
            androidLogger(Level.DEBUG)

            modules(
                deepLinkLauncherModule(
                    application = application,
                )
            )
        }.koin
    }

    fun importAppDeepLinks(deepLinkListJson: String) {
        val moshi = koin.get<Moshi>()
        val moshiAdapter = moshi.adapter(DeepLinkList::class.java)
        val appDeepLinkUseCase = koin.get<AppDeepLinkUseCase>()
        scope.launch {
            moshiAdapter.fromJson(deepLinkListJson)?.let {
                appDeepLinkUseCase.deleteAll()
                appDeepLinkUseCase.insertAll(it)
            }
        }
    }

    fun show() {
        val context = koin.get<Context>()
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
