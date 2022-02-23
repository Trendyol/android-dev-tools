package com.trendyol.devtools.environmentmanager.internal.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.trendyol.devtools.environmentmanager.internal.util.FragmentViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
) = FragmentViewBindingDelegate(this, viewBindingFactory)
