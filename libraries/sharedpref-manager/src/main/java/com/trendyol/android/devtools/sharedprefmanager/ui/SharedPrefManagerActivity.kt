package com.trendyol.android.devtools.sharedprefmanager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.trendyol.android.devtools.sharedprefmanager.di.SharedPrefManagerKoinComponent
import com.trendyol.android.devtools.sharedprefmanager.navigation.NavigationComponent
import com.trendyol.android.devtools.sharedprefmanager.ui.ui.theme.DevToolsTheme
import embedded.koin.androidx.scope.RetainedScopeActivity
import embedded.koin.androidx.viewmodel.ext.android.viewModel

internal class SharedPrefManagerActivity :
    RetainedScopeActivity(),
    SharedPrefManagerKoinComponent {

    private val sharedPrefManagerViewModel: SharedPrefManagerViewModel by viewModel()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            DevToolsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NavigationComponent(
                        viewModel = sharedPrefManagerViewModel,
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                    )
                }
            }
        }
    }
}
