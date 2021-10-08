package com.trendyol.devtools

import android.text.InputType
import com.trendyol.devtools.autofill.api.InputAdapter

class RegisterInputAdapter(
    private val email: String,
    private val name: String,
    private val password: String,
) : InputAdapter() {

    override val inputTypes: Map<Int, String>
        get() = mapOf(
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS to email,
            InputType.TYPE_TEXT_VARIATION_PERSON_NAME to name,
            InputType.TYPE_TEXT_VARIATION_PASSWORD to password
        )
}
