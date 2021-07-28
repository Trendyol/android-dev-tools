package com.trendyol.devtools.httpdebug.internal.model

import okhttp3.Response

internal data class HttpDebugResponse(var response: Response?, var state: State)
