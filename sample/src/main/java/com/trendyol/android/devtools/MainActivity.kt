package com.trendyol.devtools

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.R
import com.trendyol.android.devtools.mock_interceptor.MockInterceptor
import com.trendyol.android.devtools.mock_interceptor.internal.ext.readString
import com.trendyol.devtools.ui.main.MainFragment
import kotlin.concurrent.fixedRateTimer
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initMockServerTest()
        if (savedInstanceState == null) {
            navigateToFragment(MainFragment.newInstance())
        }
    }

    fun navigateToFragment(fragment: Fragment, backStack: String? = null) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment).apply {
                if (backStack.isNullOrBlank().not()) addToBackStack(backStack)
            }
            .commit()
    }

    private fun initMockServerTest() {
        val client = OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(this))
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val moshiAdapter = moshi.adapter(DummyBody::class.java)

        val body = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }"
        val reqBody = body.toRequestBody("application/json; charset=utf-8".toMediaType())
        val req = Request.Builder().url("https://jsonplaceholder.typicode.com/posts")
            .addHeader("header1", "test")
            .addHeader("header2", "test")
            .addHeader("header1", "test")
            .addHeader("Content-type", "application/json; charset=UTF-8")
            .post(reqBody)
            .build()

        fixedRateTimer("timer", false, 3000L, 2 * 1000) {
            client.newCall(req).enqueue(object : Callback {
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
