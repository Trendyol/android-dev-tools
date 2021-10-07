package com.trendyol.devtools.autofill.api

import com.trendyol.devtools.autofill.internal.adapter.LoginEmailInputAdapter
import com.trendyol.devtools.autofill.internal.adapter.LoginPhoneInputAdapter

sealed class AutofillData(
    val adapter: InputAdapter,
    val title: String
) {

    data class LoginEmail(
        val email: String,
        val password: String
    ) : AutofillData(LoginEmailInputAdapter(email, password), email)

    data class LoginPhone(
        val phone: String,
        val password: String
    ) : AutofillData(LoginPhoneInputAdapter(phone, password), phone)
}
