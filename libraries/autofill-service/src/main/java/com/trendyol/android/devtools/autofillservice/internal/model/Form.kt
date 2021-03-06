package com.trendyol.android.devtools.autofillservice.internal.model

import com.squareup.moshi.Json

internal data class Form(
    @Json(name = "fields") val fields: List<String>,
    @Json(name = "categories") val categories: Map<String, List<FillData>>,
)
