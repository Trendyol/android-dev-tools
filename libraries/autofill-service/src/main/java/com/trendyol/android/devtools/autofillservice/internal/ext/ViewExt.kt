package com.trendyol.android.devtools.autofillservice.internal.ext

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getAllSemanticsNodes
import androidx.compose.ui.semantics.getOrNull
import androidx.core.view.children
import com.trendyol.android.devtools.autofillservice.internal.model.AutofillInput
import com.trendyol.android.devtools.autofillservice.internal.model.ComposeAutofillInput
import com.trendyol.android.devtools.autofillservice.internal.model.ViewAutofillInput

@Suppress("VisibleForTests")
internal fun View.findAllInputs(): List<AutofillInput> {
    return mutableListOf<AutofillInput>().apply {
        when (this@findAllInputs) {
            is EditText -> {
                val inputId = if (id == View.NO_ID) {
                    null
                } else {
                    try {
                        resources.getResourceEntryName(id)
                    } catch (_: Resources.NotFoundException) {
                        null
                    }
                }
                if (inputId != null) add(ViewAutofillInput(this@findAllInputs, inputId))
            }
            is ViewRootForTest -> {
                semanticsOwner.getAllSemanticsNodes(mergingEnabled = true).forEach { node ->
                    val inputId = node.config.getOrNull(SemanticsProperties.TestTag)
                    if (!inputId.isNullOrBlank() && node.config.getOrNull(SemanticsActions.SetText)?.action != null) {
                        add(ComposeAutofillInput(this@findAllInputs, node, inputId))
                    }
                }
                (this@findAllInputs as? ViewGroup)?.children?.forEach { child ->
                    addAll(child.findAllInputs())
                }
            }
            is ViewGroup -> {
                children.forEach { child ->
                    addAll(child.findAllInputs())
                }
            }
        }
    }
}
