package com.trendyol.devtools.autofill.internal.model

import com.squareup.moshi.Json

data class AutofillWrapperEntity(
    @Json(name = "data") val data: List<AutofillEntity>,
)
