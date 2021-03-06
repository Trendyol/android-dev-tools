package com.trendyol.android.devtools.autofillservice.internal.ext

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children

fun View.findAllInputs(): List<EditText> {
    return mutableListOf<EditText>().apply {
        when (this@findAllInputs) {
            is EditText -> add(this@findAllInputs)
            is ViewGroup -> {
                children.forEach { child ->
                    addAll(child.findAllInputs())
                }
            }
        }
    }
}
