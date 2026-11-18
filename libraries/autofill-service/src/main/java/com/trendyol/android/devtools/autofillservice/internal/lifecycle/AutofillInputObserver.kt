package com.trendyol.android.devtools.autofillservice.internal.lifecycle

import android.view.View
import android.view.ViewTreeObserver
import com.trendyol.android.devtools.autofillservice.internal.ext.findAllInputs
import com.trendyol.android.devtools.autofillservice.internal.model.AutofillInput

internal class AutofillInputObserver(
    private val view: View,
    private val onInputsChanged: (Map<String, AutofillInput>) -> Unit,
    private val onInputsRemoved: (Map<String, String?>) -> Unit,
) : ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

    var inputs: Map<String, AutofillInput> = emptyMap()
        private set

    private var values: Map<String, String?> = emptyMap()

    init {
        view.addOnAttachStateChangeListener(this)
        if (view.isAttachedToWindow) view.viewTreeObserver.addOnPreDrawListener(this)
    }

    override fun onPreDraw(): Boolean {
        refresh()
        return true
    }

    fun refresh() {
        val newInputs = view.findAllInputs().associateBy { it.id }
        val changed = inputs.mapValues { it.value.identity } != newInputs.mapValues { it.value.identity }
        if (changed) onInputsRemoved(values)
        inputs = newInputs
        values = inputs.mapValues { it.value.value }
        if (changed) onInputsChanged(inputs)
    }

    override fun onViewAttachedToWindow(view: View) {
        view.viewTreeObserver.addOnPreDrawListener(this)
    }

    override fun onViewDetachedFromWindow(view: View) {
        clear()
        view.viewTreeObserver.removeOnPreDrawListener(this)
    }

    fun dispose() {
        clear()
        view.removeOnAttachStateChangeListener(this)
        if (view.viewTreeObserver.isAlive) view.viewTreeObserver.removeOnPreDrawListener(this)
    }

    private fun clear() {
        onInputsRemoved(values)
        inputs = emptyMap()
        values = emptyMap()
        onInputsChanged(inputs)
    }
}
