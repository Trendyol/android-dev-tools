package com.trendyol.devtools.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.trendyol.devtools.R
import com.trendyol.devtools.TrendyolDevTools

class MainFragment : Fragment() {

    private val button by lazy { view?.findViewById<View>(R.id.button)!! }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        button.setOnClickListener {
            TrendyolDevTools.show()
        }
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}
