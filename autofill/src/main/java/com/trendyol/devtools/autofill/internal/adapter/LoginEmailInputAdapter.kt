package com.trendyol.devtools.autofill.internal.adapter

import android.text.InputType
import com.trendyol.devtools.autofill.api.InputAdapter

class LoginEmailInputAdapter(
    private val email: String,
    private val password: String
) : InputAdapter() {

    override val inputTypes: Map<Int, String>
        get() = mapOf(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS to email,
            InputType.TYPE_TEXT_VARIATION_PASSWORD to password
        )
}
