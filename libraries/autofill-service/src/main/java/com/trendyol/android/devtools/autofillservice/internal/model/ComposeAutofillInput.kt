package com.trendyol.android.devtools.autofillservice.internal.model

import android.view.View
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.text.AnnotatedString

@OptIn(ExperimentalComposeUiApi::class)
internal class ComposeAutofillInput(
    private val view: View,
    private val node: SemanticsNode,
    override val id: String,
) : AutofillInput {
    override val identity: Any get() = view to node.id

    override val value: String?
        get() {
            val inputText = node.config.firstOrNull { it.key.name == "InputText" }?.value
            if (inputText is AnnotatedString) return inputText.text
            if (node.config.contains(SemanticsProperties.Password)) return null
            return node.config.getOrNull(SemanticsProperties.EditableText)?.text
        }

    override val isShown: Boolean
        get() = view.isShown && !node.boundsInWindow.isEmpty &&
            generateSequence(node) { it.parent }.none { it.config.contains(SemanticsProperties.InvisibleToUser) } &&
            !node.config.contains(SemanticsProperties.Disabled)

    override fun fill(value: String) {
        node.config.getOrNull(SemanticsActions.SetText)?.action?.invoke(AnnotatedString(value))
    }
}
