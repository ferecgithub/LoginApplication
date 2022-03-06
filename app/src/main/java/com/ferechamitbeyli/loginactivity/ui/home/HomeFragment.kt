package com.ferechamitbeyli.loginactivity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ferechamitbeyli.loginactivity.R
import com.ferechamitbeyli.loginactivity.databinding.FragmentHomeBinding
import com.ferechamitbeyli.loginactivity.databinding.FragmentLoginBinding
import com.ferechamitbeyli.loginactivity.viewmodel.HomeViewModel
import com.ferechamitbeyli.loginactivity.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

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

        setupOnClickListeners()
        observeViewModel()

    }

    private fun setupOnClickListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMenu_item_logOut -> {
                    viewModel.logOut()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.currentUserEmail.onEach {
                Timber.d(it)
                binding.homeFragmentTextViewWelcome.text =
                    resources.getString(R.string.homeFragment_welcome, it)
            }.launchIn(this)
        }
    }

}