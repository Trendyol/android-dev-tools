package com.trendyol.devtools.autofill.api

import android.widget.EditText
import com.trendyol.devtools.autofill.internal.ext.foreachMap
import com.trendyol.devtools.autofill.internal.ext.hasInputType

abstract class InputAdapter {

    abstract val inputTypes: Map<Int, String>

    fun isCompatibleWith(inputs: List<EditText>): Boolean {
        return inputTypes.keys
            .map { inputType -> inputType to inputs.containsInputType(inputType) }
            .toMap()
            .all { map -> map.value }
    }

    fun fill(inputs: List<EditText>) {
        inputs.forEach { input ->
            inputTypes.foreachMap { inputType, value ->
                if (input.hasInputType(inputType)) input.setText(value)
            }
        }
    }

    private fun List<EditText>.containsInputType(inputType: Int): Boolean {
        return any { input -> input.inputType and inputType == inputType }
    }
}
