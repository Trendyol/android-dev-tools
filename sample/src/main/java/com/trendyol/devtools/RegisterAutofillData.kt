package com.trendyol.devtools

import com.trendyol.devtools.autofill.api.model.AutofillData

class RegisterAutofillData(
    private val email: String,
    private val name: String,
    private val password: String,
) : AutofillData(RegisterInputAdapter(email, name, password), email)
