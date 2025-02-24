package com.trendyol.android.devtools.sharedprefmanager.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefListScreen
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefEditScreen
import com.trendyol.android.devtools.sharedprefmanager.ui.SharedPrefManagerViewModel

@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: SharedPrefManagerViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScreenType.LIST.name,
    ) {
        composable(
            route = ScreenType.LIST.name,
        ) {
            SharedPrefListScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable(
            route = ScreenType.EDIT.name,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            },
        ) {
            SharedPrefEditScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
}
