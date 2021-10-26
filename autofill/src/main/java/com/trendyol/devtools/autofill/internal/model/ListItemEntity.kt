package com.trendyol.devtools.autofill.internal.model

import com.squareup.moshi.Json

data class ListItemEntity(
    @Json(name = "fields") val fields: List<String>,
    @Json(name = "values") val values: List<String>,
)
