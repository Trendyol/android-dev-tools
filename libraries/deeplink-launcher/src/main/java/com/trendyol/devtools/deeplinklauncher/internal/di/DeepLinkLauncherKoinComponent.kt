package com.trendyol.devtools.deeplinklauncher.internal.di

import com.trendyol.devtools.deeplinklauncher.DeepLinkLauncher
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent

internal interface DeepLinkLauncherKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return DeepLinkLauncher.koin
    }
}
