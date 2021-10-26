package com.trendyol.devtools.autofill.internal.ext

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.children
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun View.findAllInputs() = withContext(Dispatchers.Main) {
    findAllInputsRecursive()
}

private fun View.findAllInputsRecursive(): List<EditText> {
    return mutableListOf<EditText>().apply {
        when (this@findAllInputsRecursive) {
            is EditText -> add(this@findAllInputsRecursive)
            is ViewGroup -> {
                children.forEach { child ->
                    addAll(child.findAllInputsRecursive())
                }
            }
        }
    }
}
