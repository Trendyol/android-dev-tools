package com.trendyol.devtools.deeplinklauncher

import android.app.Application
import android.content.Intent
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.devtools.deeplinklauncher.internal.data.model.DeepLinkList
import com.trendyol.devtools.deeplinklauncher.internal.di.ContextContainer
import com.trendyol.devtools.deeplinklauncher.internal.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object DeepLinkLauncher {

    private val moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    private val superVisorJob = SupervisorJob()
    private val scope = CoroutineScope(superVisorJob + Dispatchers.IO)

    fun init(
        application: Application
    ) {
        ContextContainer.setContext(application)
    }

    fun importAppDeepLinks(deepLinkListJson: String) {
        val moshiAdapter = moshi.adapter(DeepLinkList::class.java)
        val useCase = ContextContainer.appDeepLinkUseCase
        scope.launch {
            moshiAdapter.fromJson(deepLinkListJson)?.let {
                useCase.deleteAll()
                useCase.insertAll(it)
            }
        }
    }

    fun show() {
        val context = ContextContainer.getContext()
        context.startActivity(
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
