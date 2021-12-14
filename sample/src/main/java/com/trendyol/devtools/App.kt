package com.trendyol.android.devtools

import android.app.Application
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.autofill.AutofillService
import com.trendyol.android.devtools.internal.debugmenu.DebugActionItem
import com.trendyol.android.devtools.mock_interceptor.MockInterceptor
import com.trendyol.android.devtools.mock_interceptor.internal.ext.readString
import com.trendyol.devtools.DummyBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.fixedRateTimer

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TrendyolDevTools.init(this)
        TrendyolDevTools.addDebugAction(DebugActionItem("Toggle SSL Pinning"))

        AutofillService.Builder(this)
            .withFilePath("autofill.json")
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(this))
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val moshiAdapter = moshi.adapter(DummyBody::class.java)

        val body = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }"
        val reqBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), body)
        val req = Request.Builder().url("https://jsonplaceholder.typicode.com/posts")
            .addHeader("header1", "sdfdsfds")
            .addHeader("header2", "sdfds")
            .addHeader("header1", "435435")
            .addHeader("Content-type", "application/json; charset=UTF-8")
            .post(reqBody)
            .build()

        fixedRateTimer("timer", false, 3000L, 2 * 1000) {
            client.newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("###", "request fail: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val dummyBody = moshiAdapter.fromJson(response.body.readString())
                    Log.d("###", "request success: $dummyBody")
                }
            })
        }
    }
}
