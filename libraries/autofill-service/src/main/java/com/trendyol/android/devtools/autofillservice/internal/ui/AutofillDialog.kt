package com.trendyol.android.devtools.autofillservice.internal.ui

import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import android.view.Window
import com.trendyol.android.devtools.autofillservice.databinding.DialogAutofillBinding
import com.trendyol.android.devtools.autofillservice.internal.model.ListItem

internal class AutofillDialog(activity: Activity) : Dialog(activity) {

    private val adapter = AutofillAdapter()

    var onItemClickedListener: ((ListItem) -> Unit)? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogAutofillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter.onItemClickedListener = {
            onItemClickedListener?.invoke(it)
        }

        binding.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun updateItems(items: List<ListItem>) {
        adapter.updateItems(items)
    }
}
