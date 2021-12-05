package com.trendyol.android.devtools.autofill.internal.model

import com.squareup.moshi.Json

data class FillData(
    @Json(name = "description") val description: String,
    @Json(name = "values") val values: List<String>,
)
