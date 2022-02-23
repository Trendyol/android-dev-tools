package com.trendyol.android.devtools.httpinspector

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class MockInterceptor(context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
