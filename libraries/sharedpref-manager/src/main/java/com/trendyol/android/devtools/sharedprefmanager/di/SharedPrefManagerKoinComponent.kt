package com.trendyol.android.devtools.sharedprefmanager.di

import com.trendyol.android.devtools.sharedprefmanager.SharedPrefManager
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent

internal interface SharedPrefManagerKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return SharedPrefManager.koin
    }
}
