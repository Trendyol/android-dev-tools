package com.trendyol.android.devtools.sharedprefmanager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import embedded.koin.android.scope.AndroidScopeComponent
import embedded.koin.androidx.scope.activityRetainedScope
import embedded.koin.androidx.viewmodel.ext.android.viewModel
import embedded.koin.core.scope.Scope

internal class SharedPrefManagerActivity :
    ComponentActivity(),
    SharedPrefManagerKoinComponent,
    AndroidScopeComponent {

    override val scope: Scope by activityRetainedScope()

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
