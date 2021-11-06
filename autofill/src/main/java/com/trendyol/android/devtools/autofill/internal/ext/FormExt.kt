package com.trendyol.android.devtools.autofill.internal.ext

import com.trendyol.android.devtools.autofill.internal.model.Form
import com.trendyol.android.devtools.autofill.internal.model.ListItem

internal fun Form.getAutofillListItems(
    category: String
): List<ListItem> {
    return categories[category].orEmpty().map { values ->
        ListItem.Autofill(values.first(), values)
    }
}

internal fun Form.getCategoryListItems(): List<ListItem> {
    return categories.keys.map { name ->
        ListItem.Category(name)
    }
}
