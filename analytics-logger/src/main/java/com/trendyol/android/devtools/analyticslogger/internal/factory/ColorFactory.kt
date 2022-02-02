package com.trendyol.android.devtools.analyticslogger.internal.factory

import android.graphics.Color

object ColorFactory {

    private val colors = listOf(
        "#32E67E22",
        "#322980B9",
        "#321ABC9C",
        "#329B59B6",
        "#32F1C40F",
        "#32E74C3C",
        "#3216A085",
        "#32BDC3C7",
        "#3227AE60",
        "#32686DE0",
        "#3222A6B3",
    )

    fun getColor(value: String): Int {
        return Color.parseColor(colors[value.toList().map { it.code }.sum().mod(colors.size)])
    }
}
