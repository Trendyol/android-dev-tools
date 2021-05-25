package com.trendyol.devtools.internal.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.trendyol.devtools.R
import com.trendyol.devtools.databinding.DevToolsActivityMainBinding
import com.trendyol.devtools.internal.di.ContextContainer
import com.trendyol.uicomponents.dialogs.selectionDialog

internal class MainActivity : AppCompatActivity() {

    private lateinit var binding: DevToolsActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ContextContainer.mainContainer.MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DevToolsActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setUpView()
    }

    private fun observeViewModel() {
        viewModel.getShowEnvironmentSelectionLiveEvent().observe(this) { environments ->
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
        }.showDialog(supportFragmentManager)
    }
}
