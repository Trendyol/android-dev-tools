package com.trendyol.devtools.deeplinklauncher.internal.ui.list

import androidx.lifecycle.ViewModel
import com.trendyol.devtools.deeplinklauncher.internal.lifecycle.SingleLiveEvent

class DeepLinkListSharedViewModel : ViewModel() {
    val selectedDeepLink = SingleLiveEvent<String>()
}
