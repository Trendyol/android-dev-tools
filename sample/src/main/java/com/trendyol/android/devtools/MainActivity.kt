package com.trendyol.android.devtools

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trendyol.android.devtools.httpinspector.MockInterceptor
import com.trendyol.android.devtools.ui.main.MainFragment
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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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

        val body = "{\"username\": \"admin\", \"password\": \"123456\" }"
        val reqBody = body.toRequestBody("application/json; charset=utf-8".toMediaType())
        val req = Request.Builder().url("https://jsonplaceholder.typicode.com/posts")
            .addHeader("TestHeader1", "value")
            .addHeader("TestHeader2", "value")
            .addHeader("Content-type", "application/json; charset=UTF-8")
            .post(reqBody)
            .build()

        fixedRateTimer("timer", false, 3000L, 2000) {
            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("###", "request fail: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val dummyBody = runCatching {
                        moshiAdapter.fromJson(response.body?.string().orEmpty())
                    }.getOrNull()
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
