package com.trendyol.devtools.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trendyol.android.devtools.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(
        inflater,
        container,
        false
    ).also { binding = it }.root

    companion object {
        const val FRAGMENT_TAG = "loginFragment"
        fun newInstance() = LoginFragment()
    }
}
