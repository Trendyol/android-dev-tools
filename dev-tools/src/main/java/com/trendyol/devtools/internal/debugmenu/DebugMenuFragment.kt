package com.trendyol.devtools.internal.debugmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.trendyol.devtools.R
import com.trendyol.devtools.databinding.DevToolsFragmentDebugMenuBinding
import com.trendyol.devtools.internal.fragment.viewBinding

class DebugMenuFragment : Fragment(R.layout.dev_tools_fragment_debug_menu) {

    private val binding: DevToolsFragmentDebugMenuBinding by viewBinding(DevToolsFragmentDebugMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): DebugMenuFragment {
            return DebugMenuFragment()
        }
    }
}
