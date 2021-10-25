package com.trendyol.devtools.autofill.internal.ui

import android.os.Parcelable
import com.trendyol.devtools.autofill.internal.model.ListItem
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class AutofillDialogArguments(
    val items: List<ListItem>,
) : Parcelable