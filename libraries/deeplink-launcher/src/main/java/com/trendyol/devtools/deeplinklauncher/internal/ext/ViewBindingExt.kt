package com.trendyol.devtools.deeplinklauncher.internal.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.trendyol.devtools.deeplinklauncher.internal.util.FragmentViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
) = FragmentViewBindingDelegate(this, viewBindingFactory)
