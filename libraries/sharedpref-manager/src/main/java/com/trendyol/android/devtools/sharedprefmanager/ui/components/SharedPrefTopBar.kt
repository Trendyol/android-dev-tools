package com.trendyol.android.devtools.sharedprefmanager.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trendyol.android.devtools.sharedprefmanager.R

@Composable
fun SharedPrefTopBar(
    onDeleteClicked: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    Surface(shadowElevation = 3.dp) {
        var isSearchOpened by remember { mutableStateOf(false) }
        var searchedSharedPrefKey by remember { mutableStateOf(TextFieldValue("")) }
        val animationDuration = 500

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            AnimatedVisibility(
                visible = isSearchOpened,
                enter = expandHorizontally(
                    animationSpec = tween(durationMillis = animationDuration),
                    expandFrom = Alignment.End,
                ),
                exit = shrinkHorizontally(
                    animationSpec = tween(durationMillis = animationDuration),
                    shrinkTowards = Alignment.End,
                ),
            ) {
                OpenedTopAppBar(
                    text = searchedSharedPrefKey.text,
                    onTextChange = {
                        searchedSharedPrefKey = TextFieldValue(it)
                        onTextChange.invoke(it)
                    },
                    onCloseClicked = { isSearchOpened = false },
                )
            }
        }
        AnimatedVisibility(
            visible = isSearchOpened.not(),
            enter = expandHorizontally(animationSpec = tween(animationDuration), expandFrom = Alignment.Start),
            exit = shrinkHorizontally(animationSpec = tween(animationDuration), shrinkTowards = Alignment.Start),
        ) {
            ClosedTopAppBar(
                onOpenSearchClicked = { isSearchOpened = true },
                onDeleteClicked = onDeleteClicked,
            )
        }
    }
}

@Composable
fun OpenedTopAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onGloballyPositioned { focusRequester.requestFocus() },
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                modifier = Modifier.alpha(20F),
                text = stringResource(id = R.string.hint_search_shared_ref_key),
                color = Color.Black,
            )
        },
        singleLine = true,
        leadingIcon = {
            IconButton(
                modifier = Modifier.alpha(20F),
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.content_description_search),
                    tint = Color.Black,
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.content_description_close),
                    tint = Color.Black,
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onTextChange(text)
            },
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
fun ClosedTopAppBar(
    onOpenSearchClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            text = stringResource(id = R.string.title_activity_shared_pref_manager),
        )

        IconButton(onClick = { onOpenSearchClicked.invoke() }) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.content_description_search),
                tint = Color.Black,
            )
        }

        IconButton(onClick = { onDeleteClicked.invoke() }) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = stringResource(id = R.string.content_description_delete),
                tint = Color.Black,
            )
        }
    }
}
