package com.trendyol.android.devtools.sharedprefmanager.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel

@Composable
fun SharedPrefKeys(
    onItemClicked: (String) -> Unit,
    keys: List<SharedPrefModel>,
    innerPadding: PaddingValues,
) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(items = keys) { key ->
            SharedPrefKeyItem(
                onItemClicked = onItemClicked,
                item = key,
            )
        }
    }
}

@Composable
fun SharedPrefKeyItem(
    item: SharedPrefModel,
    onItemClicked: (String) -> Unit,
) {

    var showSharedPrefValue by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = { onItemClicked.invoke(item.key) },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {

                val itemRotation = animateFloatAsState(targetValue = if(showSharedPrefValue) 180f else 0f)

                Text(
                    text = item.key,
                    modifier = Modifier.padding(10.dp).weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                IconButton(onClick = { showSharedPrefValue = showSharedPrefValue.not() }) {
                    Icon(
                        modifier = Modifier.graphicsLayer {
                            rotationZ = itemRotation.value
                        },
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Show Shared Pref Detail",
                    )
                }
            }
            if (showSharedPrefValue) {
                Text(
                    fontSize = 12.sp,
                    text = item.value.toString(),
                    modifier = Modifier.padding(10.dp),
                )
            }
        }

    }
}
