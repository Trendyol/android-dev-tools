package com.trendyol.devtools.environmentmanager.internal.di

import com.trendyol.devtools.environmentmanager.EnvironmentManager
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent

internal interface EnvironmentManagerKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return EnvironmentManager.koin
    }
}
