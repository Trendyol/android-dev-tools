package com.trendyol.android.devtools.internal.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.infinum.dbinspector.DbInspector
import com.trendyol.android.devtools.R
import com.trendyol.android.devtools.databinding.DevToolsFragmentMainBinding
import com.trendyol.android.devtools.internal.debugmenu.DebugMenuFragment
import com.trendyol.android.devtools.internal.di.ContextContainer
import com.trendyol.android.devtools.internal.fragment.viewBinding
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
        buttonDebugPage.setOnClickListener {
            navigateToDebugMenu()
        }
        buttonInspectDatabase.setOnClickListener {
            DbInspector.show()
        }
    }

    private fun navigateToDebugMenu() {
        val fragment = DebugMenuFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showEnvironmentSelectionDialog(environmentsPair: List<Pair<Boolean, String>>) {
        selectionDialog {
            title = getString(R.string.title_select_environment)
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
