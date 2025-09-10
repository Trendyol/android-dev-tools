package com.trendyol.android.devtools.debugmenu.internal.di

import com.trendyol.android.devtools.debugmenu.DebugMenu
import embedded.koin.core.Koin
import embedded.koin.core.component.KoinComponent

internal interface DebugMenuKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return DebugMenu.koin
    }
}
