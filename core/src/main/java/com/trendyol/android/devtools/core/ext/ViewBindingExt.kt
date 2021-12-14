package com.trendyol.android.devtools.core.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.trendyol.android.devtools.core.binding.FragmentViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
) = FragmentViewBindingDelegate(this, viewBindingFactory)
