package com.trendyol.android.devtools.sharedprefmanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.trendyol.android.devtools.sharedprefmanager.R

@Composable
fun CommonTextDialog(
    text: String,
    icon: ImageVector,
    iconTint: Color,
    showDismissButton: Boolean = false,
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.wrapContentHeight(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight(),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(id = R.string.content_description_done),
                    tint = iconTint,
                )

                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 24.dp),
                    textAlign = TextAlign.Center,
                )

                if (showDismissButton) {
                    TextButton(onClick = onDismissRequest, modifier = Modifier.padding(top = 16.dp)) {
                        Text(text = stringResource(id = R.string.button_text_common_dialog))
                    }
                }
            }
        }
    }
}
