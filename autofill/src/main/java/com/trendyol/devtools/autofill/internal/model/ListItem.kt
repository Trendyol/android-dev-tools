package com.trendyol.devtools.autofill.internal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal sealed class ListItem : Parcelable {

    @Parcelize
    data class Category(val name: String) : ListItem()

    @Parcelize
    data class Autofill(
        val name: String,
        val data: List<String>,
    ) : ListItem()
}
