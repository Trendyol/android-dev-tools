package com.trendyol.devtools.autofill.api.model

import com.trendyol.devtools.autofill.api.InputAdapter
import com.trendyol.devtools.autofill.internal.adapter.LoginEmailInputAdapter
import com.trendyol.devtools.autofill.internal.adapter.LoginPhoneInputAdapter

abstract class AutofillData(
    val adapter: InputAdapter,
    val title: String
)
