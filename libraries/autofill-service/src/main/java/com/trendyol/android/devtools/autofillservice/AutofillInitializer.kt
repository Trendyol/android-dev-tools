package com.trendyol.android.devtools.autofillservice

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.trendyol.android.devtools.autofillservice.internal.AutofillProcessor

class AutofillInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        AutofillProcessor(
            application = context.applicationContext as Application,
            filePath = "autofill.json"
        )
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}
