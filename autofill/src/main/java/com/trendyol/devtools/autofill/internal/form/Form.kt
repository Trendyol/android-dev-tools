package com.trendyol.devtools.autofill.internal.form

import com.squareup.moshi.Json

data class Form(
    @Json(name = "fields") val fields: List<String>,
    @Json(name = "categories") val categories: Map<String, List<List<String>>>,
)
