package com.trendyol.devtools.httpdebug.internal.ui.detail

import com.trendyol.devtools.common.BaseViewModel
import com.trendyol.devtools.httpdebug.internal.domain.ManipulatorUseCase

internal class DetailViewModel(private val manipulatorUseCase: ManipulatorUseCase) : BaseViewModel() {

    fun skipRequest(id: String, isRequestTab: Boolean) {
        if (isRequestTab) {
            manipulatorUseCase.interceptRequest(id) { it }
        } else {
            manipulatorUseCase.interceptResponse(id) { it }
        }
    }
}
