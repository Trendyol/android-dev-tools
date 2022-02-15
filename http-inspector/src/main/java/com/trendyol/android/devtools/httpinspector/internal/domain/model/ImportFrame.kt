package com.trendyol.android.devtools.httpinspector.internal.domain.model

internal sealed class ImportFrame {

    data class Text(val content: String) : ImportFrame()

    object Close : ImportFrame()
}
