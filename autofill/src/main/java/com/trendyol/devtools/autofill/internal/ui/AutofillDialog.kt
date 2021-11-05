package com.trendyol.devtools.autofill.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.trendyol.devtools.autofill.databinding.DialogAutofillBinding
import com.trendyol.devtools.autofill.internal.model.ListItem

internal class AutofillDialog : DialogFragment() {

    private lateinit var binding: DialogAutofillBinding

    private val adapter by lazy { AutofillAdapter() }

    var onItemClickedListener: ((ListItem) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAutofillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        adapter.onItemClickedListener = {
            onItemClickedListener?.invoke(it)
        }

        binding.recyclerView.adapter = adapter
    }

    fun updateItems(items: List<ListItem>) {
        adapter.updateItems(items)
    }

    companion object {
        const val DIALOG_TAG = "dialogAutofill"

        fun newInstance() = AutofillDialog()
    }
}
