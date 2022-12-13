package com.trendyol.devtools.deeplinklauncher.internal.domain

class DeepLinkValidator {
    fun validate(deepLink: String): String {
        when {
            deepLink.isEmpty() -> throw EmptyNameException()
            isDeepLinkValid(deepLink).not() -> throw NotValidDeepLinkException()
        }
        return deepLink
    }

    private fun isDeepLinkValid(deepLink: String): Boolean {
        return deepLink.contains("://")
    }
}

class EmptyNameException : Throwable()
class NotValidDeepLinkException : Throwable()
