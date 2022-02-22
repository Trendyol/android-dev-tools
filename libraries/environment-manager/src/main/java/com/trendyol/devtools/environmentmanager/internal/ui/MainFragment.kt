package com.trendyol.devtools.environmentmanager.internal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trendyol.devtools.environmentmanager.R
import com.trendyol.devtools.environmentmanager.databinding.DevToolsFragmentMainBinding
import com.trendyol.devtools.environmentmanager.internal.di.ContextContainer
import com.trendyol.devtools.environmentmanager.internal.ext.viewBinding
import com.trendyol.uicomponents.dialogs.selectionDialog

internal class MainFragment : Fragment(R.layout.dev_tools_fragment_main) {

    private val binding: DevToolsFragmentMainBinding by viewBinding(DevToolsFragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpView()
    }

    private fun observeViewModel() {
        viewModel.getShowEnvironmentSelectionLiveEvent().observe(viewLifecycleOwner) { environments ->
            showEnvironmentSelectionDialog(environments)
        }
    }

    private fun setUpView() = with(binding) {
        buttonChangeEnvironment.setOnClickListener {
            viewModel.onEnvironmentChangeClicked()
        }
    }

    private fun showEnvironmentSelectionDialog(environmentsPair: List<Pair<Boolean, String>>) {
        selectionDialog {
            title = getString(R.string.dev_tools_environment_manager_title_select_environment)
            items = environmentsPair
            onItemSelectedListener = { dialog, itemIndex ->
                dialog.dismiss()
                viewModel.onEnvironmentSelected(itemIndex)
            }
            onItemReselectedListener = { dialog, _ ->
                dialog.dismiss()
            }
            selectedItemDrawable = android.R.drawable.ic_menu_add
            showCloseButton = true
        }.showDialog(childFragmentManager)
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
