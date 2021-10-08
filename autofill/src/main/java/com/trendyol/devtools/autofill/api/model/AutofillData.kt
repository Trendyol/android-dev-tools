package com.trendyol.devtools.autofill.api.model

import com.trendyol.devtools.autofill.api.InputAdapter

abstract class AutofillData(
    val adapter: InputAdapter,
    val title: String
)
