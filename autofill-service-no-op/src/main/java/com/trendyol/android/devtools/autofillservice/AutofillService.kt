package com.trendyol.android.devtools.autofillservice

import android.app.Application

class AutofillService {

    class Builder(private val application: Application) {

        fun withFilePath(filePath: String): Builder {
            return this
        }

        fun build() {

        }

    }

}
