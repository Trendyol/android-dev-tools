package com.trendyol.devtools.autofill.internal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.trendyol.devtools.autofill.databinding.DialogAutofillBinding
import com.trendyol.devtools.autofill.internal.model.ListItem

internal class AutofillDialog : DialogFragment() {

    private val args by lazy {
        requireNotNull(requireArguments().getParcelable<AutofillDialogArguments>(BUNDLE_ARGUMENTS))
    }

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
        adapter.updateItems(args.items)
    }

    fun setArguments(args: AutofillDialogArguments) {
        adapter.updateItems(args.items)
    }

    companion object {
        const val DIALOG_TAG = "dialogAutofill"
        private const val BUNDLE_ARGUMENTS = "arguments"

        fun newInstance(args: AutofillDialogArguments) = AutofillDialog().apply {
            arguments = bundleOf(BUNDLE_ARGUMENTS to args)
        }
    }
}
