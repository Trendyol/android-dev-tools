package com.trendyol.android.devtools.analyticslogger.internal.util

import android.util.Log
import android.webkit.WebView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal suspend fun WebView.executeJS(jsScript: String): String {
    val tag = "WebViewJSExecution"
    return suspendCancellableCoroutine { continuation ->
        try {
            evaluateJavascript(jsScript) { result ->
                val cleanResult = if (result != null && result.startsWith('"') && result.endsWith('"')) {
                    result.substring(1, result.length - 1)
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\")
                } else {
                    result ?: "TRANSFORM_ERROR: No result"
                }
                continuation.resume(cleanResult)
            }
            continuation.invokeOnCancellation {
                destroy()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error executing JavaScript transform", e)
            continuation.resumeWithException(e)
        }
    }
}
