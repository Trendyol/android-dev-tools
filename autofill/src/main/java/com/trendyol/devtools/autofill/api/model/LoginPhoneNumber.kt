package com.trendyol.devtools.autofill.api.model

import com.trendyol.devtools.autofill.internal.adapter.LoginPhoneInputAdapter

data class LoginPhoneNumber(
    val phone: String,
    val password: String
) : AutofillData(LoginPhoneInputAdapter(phone, password), phone)
