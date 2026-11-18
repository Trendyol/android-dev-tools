package com.trendyol.android.devtools.autofillservice.internal.model

import android.widget.EditText

internal interface AutofillInput {
    val id: String
    val identity: Any
    val value: String?
    val isShown: Boolean

    fun fill(value: String)
}

internal class ViewAutofillInput(
    private val view: EditText,
    override val id: String,
) : AutofillInput {
    override val identity: Any get() = view
    override val value: String get() = view.text.toString()
    override val isShown: Boolean get() = view.isShown

    override fun fill(value: String) {
        view.setText(value)
    }
}
