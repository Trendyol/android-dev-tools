package com.trendyol.android.devtools.debugmenu.internal.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.trendyol.android.devtools.debugmenu.internal.util.FragmentViewBindingDelegate

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
) = FragmentViewBindingDelegate(this, viewBindingFactory)
