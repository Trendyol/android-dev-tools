package com.trendyol.android.devtools.debugmenu.internal.di

import android.content.Context
import com.trendyol.android.devtools.debugmenu.internal.domain.DebugMenuUseCase
import com.trendyol.android.devtools.debugmenu.internal.ui.DebugMenuViewModel
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.core.module.Module
import embedded.koin.dsl.module

internal fun debugMenuModule(
    context: Context,
): Module = module {
    single<Context> { context }
    single { DebugMenuUseCase() }
    viewModelOf(::DebugMenuViewModel)
}
