package com.trendyol.devtools.autofill.api.model

import com.trendyol.devtools.autofill.internal.adapter.LoginEmailInputAdapter

data class LoginEmail(
    val email: String,
    val password: String
) : AutofillData(LoginEmailInputAdapter(email, password), email)
