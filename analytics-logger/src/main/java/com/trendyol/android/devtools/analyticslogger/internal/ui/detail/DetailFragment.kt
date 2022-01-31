package com.trendyol.android.devtools.analyticslogger.internal.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class DetailFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        ContextContainer.mainContainer.MainViewModelFactory()
    }

    private lateinit var textViewKey: TextView

    private lateinit var textViewValue: TextView

    private lateinit var textViewDate: TextView

    private lateinit var textViewPlatform: TextView

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
        textViewPlatform = view.findViewById(R.id.textViewPlatform)
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
            textViewValue.text = state.event.json
            textViewDate.text = state.event.date
            textViewPlatform.text = state.event.platform?.title
            textViewPlatform.background = createPlatformBackground(
                Color.parseColor(state.event.platform?.color)
            )
        }
    }

    private fun createPlatformBackground(@ColorInt colorInt: Int): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = 20f
            color = ColorStateList.valueOf(colorInt)
        }
    }

    companion object {
        const val NAME = "detailFragment"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
