package com.ferechamitbeyli.loginactivity.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ferechamitbeyli.loginactivity.R
import com.ferechamitbeyli.loginactivity.databinding.ForgotPassBottomSheetDialogBinding
import com.ferechamitbeyli.loginactivity.databinding.FragmentLoginBinding
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginErrorState
import com.ferechamitbeyli.loginactivity.utils.getColorByAttribute
import com.ferechamitbeyli.loginactivity.viewmodel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginFragmentButtonLogin.setOnClickListener {
            viewModel.login(
                binding.loginFragmentTextInputEditTextEmail.text.toString(),
                binding.loginFragmentTextInputEditTextPassword.text.toString()
            )
        }

        binding.loginFragmentButtonSignUp.setOnClickListener {
            viewModel.signUp(
                binding.loginFragmentTextInputEditTextEmail.text.toString(),
                binding.loginFragmentTextInputEditTextPassword.text.toString()
            )
        }

        binding.loginFragmentTextViewForgotPassword.setOnClickListener {
            viewModel.restorePasswordInitiated()
        }



        with(lifecycleScope) {
            viewModel.state.onEach { state ->
                Timber.d(state.toString())
                binding.loginFragmentTextInputLayoutEmail.error = if (state.isEmailValid) {
                    null
                } else {
                    resources.getString(R.string.loginFragment_invalid_email)
                }
                binding.loginFragmentTextInputLayoutPassword.error = if (state.isPasswordValid) {
                    null
                } else {
                    resources.getString(R.string.loginFragment_invalid_password)
                }
            }.launchIn(this)

            viewModel.error.onEach {
                Timber.d("Error! : $it")
                showSnackbar(
                    when (it) {
                        LoginErrorState.LOGIN -> {
                            resources.getString(R.string.loginFragment_login_failed)
                        }
                        LoginErrorState.SIGNUP -> {
                            resources.getString(R.string.loginFragment_register_failed)
                        }
                        LoginErrorState.FORGOT_PASSWORD -> {
                            resources.getString(R.string.loginFragment_restorePass_failed)
                        }
                    },
                    requireContext().getColorByAttribute(androidx.appcompat.R.attr.colorError)
                )
            }.launchIn(this)

            viewModel.registerSuccess.onEach {
                Timber.d("Register success!")
                showSnackbar(
                    resources.getString(R.string.loginFragment_register_success),
                    requireContext().getColorByAttribute(androidx.appcompat.R.attr.colorPrimary),
                    object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            viewModel.onRegistrationSnackBarDismissed()
                        }
                    }
                )
            }.launchIn(this)

            viewModel.bottomSheetShow.onEach {
                Timber.d("Showing forgot password dialog")
                showForgotPasswordBottomSheetDialog()
            }.launchIn(this)

            viewModel.forgotPassGetUserSuccess.onEach {
                Timber.d("Get password succeed")
                showSnackbar(
                    resources.getString(R.string.loginFragment_your_pass_is, it),
                    requireContext().getColorByAttribute(androidx.appcompat.R.attr.colorAccent)
                )
            }.launchIn(this)

            viewModel.navigateToApp.onEach {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }.launchIn(this)
        }

    }

    private fun showSnackbar(
        message: String,
        backgroundTint: Int,
        callback: BaseTransientBottomBar.BaseCallback<Snackbar>? = null
    ) {
        val snackBar = Snackbar.make(
            requireContext(),
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(backgroundTint)

        if (callback != null) {
            snackBar.addCallback(callback)
        }

        snackBar.show()
    }

    private fun showForgotPasswordBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val forgotPassBinding = ForgotPassBottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(forgotPassBinding.root)

        forgotPassBinding.forgotPassBottomSheetButtonSubmit.setOnClickListener {
            viewModel.restorePassword(forgotPassBinding.forgotPassBottomSheetTextInputEditTextEmail.text.toString())
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}