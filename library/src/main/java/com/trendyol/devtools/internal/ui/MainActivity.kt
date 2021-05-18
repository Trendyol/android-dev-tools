package com.trendyol.devtools.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.devtools.databinding.DevToolsActivityMainBinding
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.devtools.internal.flow.observeInLifecycle
import com.trendyol.uicomponents.dialogs.selectionDialog
import kotlinx.coroutines.flow.onEach

internal class MainActivity : AppCompatActivity() {

    private lateinit var binding: DevToolsActivityMainBinding
    private val viewModel: MainViewModel by lazy { ContextContainer.getMainContainer().getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DevToolsActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setUpView()
    }

    private fun observeViewModel() {
        viewModel.actionsFlow.onEach { action ->
            when (action) {
                is MainViewModel.Action.ShowEnvironmentSelection -> showEnvironmentSelectionDialog(action.environments)
            }
        }.observeInLifecycle(this)
    }

    private fun setUpView() = with(binding) {
        buttonChangeEnvironment.setOnClickListener {
            viewModel.onEnvironmentChangeClicked()
        }
    }

    private fun showEnvironmentSelectionDialog(environmentsPair: List<Pair<Boolean, String>>) {
        selectionDialog {
            title = "Select Env"
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
        }.showDialog(supportFragmentManager)
    }
}
