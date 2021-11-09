package com.trendyol.android.devtools.autofill.internal.model

import com.squareup.moshi.Json

internal data class Forms(
    @Json(name = "forms") val data: List<Form>,
)
