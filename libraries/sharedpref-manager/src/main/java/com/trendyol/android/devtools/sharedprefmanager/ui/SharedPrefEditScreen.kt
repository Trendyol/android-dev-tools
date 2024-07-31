package com.trendyol.android.devtools.sharedprefmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trendyol.android.devtools.sharedprefmanager.R
import com.trendyol.android.devtools.sharedprefmanager.ui.components.CommonTextDialog
import com.trendyol.android.devtools.sharedprefmanager.ui.components.ConfirmationDialog
import com.trendyol.android.devtools.sharedprefmanager.ui.components.SharedPrefEditInput
import kotlinx.coroutines.delay

@Composable
fun SharedPrefEditScreen(
    navController: NavController,
    viewModel: SharedPrefManagerViewModel,
) {
    Scaffold(
        topBar = {
            Surface(shadowElevation = 3.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back),
                        )
                    }

                    Text(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        text = stringResource(id = R.string.title_edit_screen),
                    )
                }
            }
        },

        ) { innerPadding ->

        val item = viewModel.getSelectedSharedPrefItem()
        var sharedPrefValue by remember { mutableStateOf(TextFieldValue(item.value.toString())) }
        val scrollState = rememberScrollState()

        val errorMessage by viewModel.updateError.collectAsState()
        val isUpdateCompleted by viewModel.isUpdateCompleted.collectAsState()
        val showDeleteConfirmationDialog by viewModel.showDeleteConfirmationDialog.collectAsState()

        if (errorMessage.isNotEmpty()) {
            CommonTextDialog(
                text = errorMessage,
                icon = Icons.Filled.Warning,
                iconTint = Color.Red,
                showDismissButton = true,
                onDismissRequest = { viewModel.clearUpdateError() },
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
                navController.popBackStack()
            }
        }

        if (showDeleteConfirmationDialog) {
            ConfirmationDialog(
                onDismissRequest = { viewModel.updateShowDeleteConfirmationDialog(false) },
                onConfirmation = {
                    viewModel.updateShowDeleteConfirmationDialog(false)
                    viewModel.deleteSharedPrefItem()
                },
                dialogTitle = stringResource(id = R.string.title_delete_item_dialog),
                dialogText = stringResource(
                    id = R.string.description_delete_item_dialog,
                    viewModel.getSelectedSharedPrefItem().key,
                ),
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                fontWeight = FontWeight.Medium,
                text = item.key,
            )

            SharedPrefEditInput(
                sharedPrefItem = item,
                sharedPrefValue = sharedPrefValue,
                onValueChanged = { sharedPrefValue = it },
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.updateSharedPrefItem(sharedPrefValue.text) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.button_text_edit_screen_update))
            }
            TextButton(
                onClick = { viewModel.updateShowDeleteConfirmationDialog(true) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.button_text_edit_screen_delete))
            }
        }
    }
}
