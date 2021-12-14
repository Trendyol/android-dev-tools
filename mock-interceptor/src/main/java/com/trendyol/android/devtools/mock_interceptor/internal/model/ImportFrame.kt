package com.trendyol.android.devtools.mock_interceptor.internal.model

sealed class ImportFrame {

    data class Text(val content: String) : ImportFrame()

    object Close : ImportFrame()
}
