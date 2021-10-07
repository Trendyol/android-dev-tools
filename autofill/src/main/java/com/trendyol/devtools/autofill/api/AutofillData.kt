package com.trendyol.devtools.autofill.api

data class AutofillData(
    val email: String,
    val password: String,
    val description: String,
    val environment: String? = null
)
