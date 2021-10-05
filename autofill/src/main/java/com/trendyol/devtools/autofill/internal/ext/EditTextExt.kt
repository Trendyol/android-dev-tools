package com.trendyol.devtools.autofill.internal.ext

import android.widget.EditText

fun EditText.hasInputType(inputType: Int): Boolean {
    return (this.inputType and inputType) == inputType
}
