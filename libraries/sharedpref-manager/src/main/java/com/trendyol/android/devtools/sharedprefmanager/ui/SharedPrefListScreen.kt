package com.trendyol.android.devtools.sharedprefmanager.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.trendyol.android.devtools.sharedprefmanager.R
import com.trendyol.android.devtools.sharedprefmanager.navigation.ScreenType
import com.trendyol.android.devtools.sharedprefmanager.ui.components.CommonTextDialog
import com.trendyol.android.devtools.sharedprefmanager.ui.components.ConfirmationDialog
import com.trendyol.android.devtools.sharedprefmanager.ui.components.SharedPrefTopBar
import kotlinx.coroutines.delay

@Composable
fun SharedPrefListScreen(
    navController: NavController,
    viewModel: SharedPrefManagerViewModel,
) {
    val isUpdateCompleted by viewModel.isUpdateCompleted.collectAsState()
    val showDeleteConfirmationDialog by viewModel.showDeleteConfirmationDialog.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SharedPrefTopBar(
                onDeleteClicked = {
                    viewModel.updateShowDeleteConfirmationDialog(true)
                },
                onTextChange = { viewModel.searchSharedPrefItemByKey(it) }
            )
        },
    ) { innerPadding ->

        LaunchedEffect(Unit) {
            viewModel.getAllSharedPrefs()
        }

        if(showDeleteConfirmationDialog) {
            ConfirmationDialog(
                onDismissRequest = { viewModel.updateShowDeleteConfirmationDialog(false) },
                onConfirmation = {
                    viewModel.updateShowDeleteConfirmationDialog(false)
                    viewModel.deleteAllSharedPref()
                },
                dialogTitle = stringResource(id = R.string.title_delete_all_dialog),
                dialogText = stringResource(id = R.string.description_delete_all_dialog),
            )
        }

        if (isUpdateCompleted) {
            CommonTextDialog(
                text = stringResource(id = R.string.text_success_dialog),
                icon = Icons.Filled.CheckCircle,
                iconTint = Color.Green
            )
            LaunchedEffect(Unit) {
                delay(1500L)
                viewModel.getAllSharedPrefs()
                viewModel.clearUpdateCompleted()
            }
        }

        SharedPrefKeys(
            onItemClicked = {
                viewModel.updateSelectedSharedPrefKey(it)
                navController.navigate(ScreenType.EDIT.name)
            },
            keys = viewModel.sharedPrefList,
            innerPadding = innerPadding,
        )
    }
}
