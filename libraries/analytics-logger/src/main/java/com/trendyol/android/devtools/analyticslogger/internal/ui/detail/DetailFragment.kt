package com.trendyol.android.devtools.analyticslogger.internal.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.trendyol.android.devtools.analyticslogger.AnalyticsLogger
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.databinding.AnalyticsLoggerFragmentDetailBinding
import com.trendyol.android.devtools.analyticslogger.internal.di.AnalyticsLoggerKoinComponent
import com.trendyol.android.devtools.analyticslogger.internal.domain.usecase.ExcludeKeysUseCase
import com.trendyol.android.devtools.analyticslogger.internal.factory.ColorFactory
import com.trendyol.android.devtools.analyticslogger.internal.ui.MainViewModel
import com.trendyol.android.devtools.analyticslogger.internal.util.executeJS
import embedded.koin.android.ext.android.inject
import embedded.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlinx.coroutines.launch

internal class DetailFragment : Fragment(), AnalyticsLoggerKoinComponent {

    private val viewModel: MainViewModel by activityViewModel()
    private val excludeKeysUseCase: ExcludeKeysUseCase by inject()

    private var _binding: AnalyticsLoggerFragmentDetailBinding? = null
    private var originalJsonText: String = ""

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AnalyticsLoggerFragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        observeData()
    }

    private fun initializeViews() = with(binding) {
        webViewJsExecutor.settings.javaScriptEnabled = true
        editTextjsTransformFunction.setText(AnalyticsLogger.getEventTransformFunction())
        editTextjsTransformFunction.doAfterTextChanged {
            AnalyticsLogger.setEventTransformFunction(it.toString())
        }
        checkboxShowJSTransformFunction.setOnCheckedChangeListener { _, isChecked ->
            editTextjsTransformFunction.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        editTextExcludeEventKeys.setText(excludeKeysUseCase.getExcludedKeys())
        editTextExcludeEventKeys.doAfterTextChanged { editable ->
            excludeKeysUseCase.saveExcludedKeys(editable.toString())
        }

        editTextHighlightEventText.doAfterTextChanged { editable ->
            highlightTextInJsonView(editable.toString())
        }
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
            textViewSource.text = state.event.source
            originalJsonText = state.event.json.orEmpty()
            highlightTextInJsonView(editTextHighlightEventText.text.toString())
            textViewDate.text = state.event.date
            textViewPlatform.text = state.event.platform
            textViewPlatform.background = createPlatformBackground(state.event.platform)
            horizontalScrollView.background = ContextCompat.getDrawable(
                /* context = */ root.context,
                /* id = */ getStatusBackgroundRes(state.event.isSuccess)
            )
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

    private fun getStatusBackgroundRes(isSuccess: Boolean?): Int {
        return when (isSuccess) {
            true -> R.drawable.analytics_logger_success_background
            false -> R.drawable.analytics_logger_failure_background
            null -> R.drawable.analytics_logger_json_background
        }
    }

    private fun highlightTextInJsonView(searchText: String) {
        if (originalJsonText.isEmpty()) {
            return
        }

        if (searchText.isEmpty()) {
            binding.textViewValue.text = originalJsonText
            return
        }

        val spannableString = SpannableString(originalJsonText)
        val searchTextLower = searchText.lowercase()
        val originalTextLower = originalJsonText.lowercase()
        val highlightColor = Color.YELLOW

        var startIndex = 0
        while (true) {
            val index = originalTextLower.indexOf(searchTextLower, startIndex)
            if (index == -1) break

            spannableString.setSpan(
                BackgroundColorSpan(highlightColor),
                index,
                index + searchText.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            startIndex = index + 1
        }

        binding.textViewValue.text = spannableString
    }

    private fun copyToClipboard() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val originalData = (viewModel.detailState.value as? DetailState.Selected)?.event?.json.orEmpty()
        val excludedKeys = excludeKeysUseCase.getExcludedKeysList()
        val filteredData = excludeKeysUseCase.filterJsonByExcludedKeys(originalData, excludedKeys)
        clipboard.setPrimaryClip(ClipData.newPlainText(CLIPBOARD_LABEL, filteredData))
        Toast.makeText(context, R.string.analytics_logger_toast_copied, Toast.LENGTH_SHORT).show()
    }

    private fun copyTransformedToClipboard() {
        val originalData = (viewModel.detailState.value as? DetailState.Selected)?.event?.json.orEmpty()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val transformedData = transformEventData(originalData)
                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(CLIPBOARD_TRANSFORMED_LABEL, transformedData))
                Toast.makeText(context, R.string.analytics_logger_toast_copied, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Transform failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun transformEventData(eventData: String): String {
        val jsFunction = AnalyticsLogger.getEventTransformFunction()
        val jsScript = with(StringBuilder()) {
            append(jsFunction)
            appendLine()
            append("transform($eventData)")
        }
        return binding.webViewJsExecutor.executeJS(jsScript.toString())
    }

    private fun copySourceToClipboard() {
        val source = (viewModel.detailState.value as? DetailState.Selected)?.event?.source.orEmpty()
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(CLIPBOARD_SOURCE_LABEL, source))
        Toast.makeText(context, R.string.analytics_logger_toast_copied, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_copy -> copyToClipboard()
            R.id.action_test_assert -> copyTransformedToClipboard()
            R.id.action_copy_source -> copySourceToClipboard()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.analytics_logger_menu_event_detail, menu)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val NAME = "detailFragment"
        private const val CLIPBOARD_LABEL = "Event Detail"
        private const val CLIPBOARD_TRANSFORMED_LABEL = "Transformed Event Detail"
        private const val CLIPBOARD_SOURCE_LABEL = "Event Source"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}
