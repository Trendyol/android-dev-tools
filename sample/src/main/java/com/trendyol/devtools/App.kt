package com.trendyol.android.devtools

import android.app.Application
import android.util.Log
import com.trendyol.android.devtools.autofill.AutofillService
import com.trendyol.android.devtools.internal.debugmenu.DebugActionItem
import com.trendyol.android.devtools.mock_interceptor.MockInterceptor
import com.trendyol.android.devtools.mock_interceptor.WebServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
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

        val bod = "{\"sa\": \"as\", \"deneme\": \"123\"}"
        val reqBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), bod)
        val req = Request.Builder().url("https://jsonplaceholder.typicode.com/todos/1")
            .post(reqBody)
            .build()

        fixedRateTimer("timer", false, 5000L, 4 * 1000) {
            client.newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("###", "req fail: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("###", "req success: ${response.body.toString()}")
                }
            })
        }

        fixedRateTimer("timer2", false, 5010L, 4 * 1000) {
            client.newCall(req).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("###", "req2 fail: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("###", "re2 success: ${response.body.toString()}")
                }
            })
        }
    }
}
