package com.trendyol.devtools.deeplinklauncher.internal.di

import android.content.Context
import com.trendyol.devtools.deeplinklauncher.internal.data.database.DeeplinkDatabase

internal class DeeplinkContainer(private val context: Context) {
    private val deeplinkDatabase: DeeplinkDatabase by lazy { DeeplinkDatabase.create(context) }
}
