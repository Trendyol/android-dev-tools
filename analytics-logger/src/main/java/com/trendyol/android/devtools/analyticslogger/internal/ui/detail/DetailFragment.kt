package com.trendyol.android.devtools.analyticslogger.internal.ui.detail

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.trendyol.android.devtools.analyticslogger.databinding.AnalyticsLoggerFragmentDetailBinding
import com.trendyol.android.devtools.analyticslogger.internal.di.ContextContainer
import com.trendyol.android.devtools.analyticslogger.internal.factory.ColorFactory
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class DetailFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels {
        ContextContainer.mainContainer.MainViewModelFactory()
    }

    private lateinit var binding: AnalyticsLoggerFragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AnalyticsLoggerFragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: DetailState) = with(binding) {
        if (state is DetailState.Selected) {
            textViewKey.text = state.event.key
            textViewValue.text = state.event.json
            textViewDate.text = state.event.date
            textViewPlatform.text = state.event.platform
            textViewPlatform.background = createPlatformBackground(state.event.platform)
        }
    }

    private fun createPlatformBackground(platform: String?): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = 20f
            color = ColorStateList.valueOf(
                ColorFactory.getColor(platform.orEmpty())
            )
        }
    }

    companion object {
        const val NAME = "detailFragment"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
