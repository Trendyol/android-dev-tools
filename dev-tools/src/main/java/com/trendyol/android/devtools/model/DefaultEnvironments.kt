package com.trendyol.android.devtools.model

object DefaultEnvironments {

    const val PRODUCTION = "prod"
    const val PRE_PRODUCTION = "pre_prod"
    const val STAGING = "stage"

    internal fun getAll(): List<String> = listOf(PRODUCTION, PRE_PRODUCTION, STAGING)
}
