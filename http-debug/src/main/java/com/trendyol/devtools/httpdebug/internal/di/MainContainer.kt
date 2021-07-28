package com.trendyol.devtools.httpdebug.internal.di

import com.trendyol.devtools.httpdebug.internal.domain.IdGeneratorUseCase
import com.trendyol.devtools.httpdebug.internal.domain.ManipulatorUseCase

internal object MainContainer {

    val manipulatorUseCase by lazy { ManipulatorUseCase() }
    val idGeneratorUseCase by lazy { IdGeneratorUseCase() }
    val requestListContainer by lazy { RequestListContainer() }
}
