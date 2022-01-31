package com.trendyol.android.devtools.analyticslogger.internal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        ContextContainer.mainContainer.MainViewModelFactory()
    }

    private lateinit var textViewKey: TextView

    private lateinit var textViewValue: TextView

    private lateinit var textViewDate: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observeData()
    }

    private fun initView(view: View) {
        textViewKey = view.findViewById(R.id.textViewKey)
        textViewValue = view.findViewById(R.id.textViewValue)
        textViewDate = view.findViewById(R.id.textViewDate)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: DetailState) {
        if (state is DetailState.Selected) {
            textViewKey.text = state.event.key
            textViewValue.text = state.event.value
            textViewDate.text = state.event.date
        }
    }

    companion object {
        const val NAME = "detailFragment"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
