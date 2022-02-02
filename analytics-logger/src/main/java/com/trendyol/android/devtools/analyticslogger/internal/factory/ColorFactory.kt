package com.trendyol.android.devtools.analyticslogger.internal.factory

import android.graphics.Color

object ColorFactory {

    private val colors = listOf(
        "#32E67E22",
        "#322980B9",
        "#321ABC9C",
        "#329B59B6",
    )

    fun getColor(value: String): Int {
        return Color.parseColor(colors[value.toList().map { it.code }.sum().mod(colors.size)])
    }
}
