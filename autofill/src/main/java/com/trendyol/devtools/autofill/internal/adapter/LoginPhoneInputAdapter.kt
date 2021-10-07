package com.trendyol.devtools.autofill.internal.adapter

import android.text.InputType
import com.trendyol.devtools.autofill.api.InputAdapter

class LoginPhoneInputAdapter(
    private val phone: String,
    private val password: String
) : InputAdapter() {

    override val inputTypes: Map<Int, String>
        get() = mapOf(
            InputType.TYPE_CLASS_PHONE to phone,
            InputType.TYPE_TEXT_VARIATION_PASSWORD to password
        )
}
