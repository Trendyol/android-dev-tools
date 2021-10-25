package com.trendyol.devtools.autofill.internal.model

import com.squareup.moshi.Json

data class Forms(
    @Json(name = "forms") val forms: List<Form>,
)
