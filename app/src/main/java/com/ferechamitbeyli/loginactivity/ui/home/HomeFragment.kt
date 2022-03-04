package com.ferechamitbeyli.loginactivity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ferechamitbeyli.loginactivity.R
import com.ferechamitbeyli.loginactivity.databinding.FragmentHomeBinding
import com.ferechamitbeyli.loginactivity.databinding.FragmentLoginBinding
import com.ferechamitbeyli.loginactivity.viewmodel.LoginViewModel

class HomeFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}