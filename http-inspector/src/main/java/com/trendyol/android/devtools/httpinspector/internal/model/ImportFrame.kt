package com.trendyol.android.devtools.httpinspector.internal.model

sealed class ImportFrame {

    data class Text(val content: String) : ImportFrame()

    object Close : ImportFrame()
}
