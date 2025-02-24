package com.trendyol.android.devtools.sharedprefmanager.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefValueType

@Composable
fun SharedPrefEditInput(
    sharedPrefItem: SharedPrefModel,
    sharedPrefValue: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
) {
    if (sharedPrefItem.valueType == SharedPrefValueType.BOOLEAN) {
        BooleanSpinner(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            preselected = sharedPrefValue.text.toBoolean(),
            onSelectionChanged = { onValueChanged.invoke(TextFieldValue(it.toString())) },
        )
    } else {
        OutlinedCard(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
            ),
            shape = MaterialTheme.shapes.medium,
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                maxLines = 10,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                value = sharedPrefValue,
                onValueChange = { onValueChanged.invoke(it) },
            )
        }
    }
}
