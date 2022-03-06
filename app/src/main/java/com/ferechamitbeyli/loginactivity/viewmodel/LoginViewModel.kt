package com.ferechamitbeyli.loginactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginErrorState
import com.ferechamitbeyli.loginactivity.utils.Result
import com.ferechamitbeyli.loginactivity.utils.isValidEmail
import com.ferechamitbeyli.loginactivity.utils.isValidPassword
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginState
import com.ferechamitbeyli.loginactivity.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val getForgottenPasswordUseCase: GetForgottenPasswordUseCase
) : ViewModel() {

    private val _bottomSheetShow = MutableSharedFlow<Unit>()
    val bottomSheetShow = _bottomSheetShow.asSharedFlow()

    private val _forgotPassGetUserSuccess = MutableSharedFlow<String>()
    val forgotPassGetUserSuccess = _forgotPassGetUserSuccess.asSharedFlow()

    private val _registerSuccess = MutableSharedFlow<Unit>()
    val registerSuccess = _registerSuccess.asSharedFlow()

    private val _navigateToApp = MutableSharedFlow<Unit>()
    val navigateToApp = _navigateToApp.asSharedFlow()

    private val _error = MutableSharedFlow<LoginErrorState>()
    val error = _error.asSharedFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        Timber.d("LoginViewModel init")
    }

    fun login(email: String, password: String) {
        Timber.d("Login: $email, $password")
        if (validateInputs(email, password)) {
            viewModelScope.launch {
                when (loginUserUseCase(email, password)) {
                    Result.Failure -> {
                        _error.emit(LoginErrorState.LOGIN)
                    }
                    Result.Success -> _navigateToApp.emit(Unit)
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        Timber.d("Sign up: $email, $password")
        if (validateInputs(email, password)) {
            viewModelScope.launch {
                when (registerUserUseCase(email, password)) {
                    Result.Failure -> {
                        _error.emit(LoginErrorState.SIGNUP)
                    }
                    Result.Success -> {
                        loginUserUseCase(email, password)
                        _registerSuccess.emit(Unit)
                    }
                }
            }
        }
    }

    fun restorePasswordInitiated() {
        Timber.d("Showing the bottom sheet dialog!")
        viewModelScope.launch {
            _bottomSheetShow.emit(Unit)
        }
    }

    fun restorePassword(email: String) {
        Timber.d("Restoring pass of $email")
        viewModelScope.launch {
            when (val result = getForgottenPasswordUseCase(email)) {
                is GetForgottenPasswordUseCase.Result.Success -> _forgotPassGetUserSuccess.emit(
                    result.password
                )
                is GetForgottenPasswordUseCase.Result.Failure -> _error.emit(LoginErrorState.FORGOT_PASSWORD)
            }

        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        val isEmailValid = email.isValidEmail()
        val isPasswordValid = password.isValidPassword()

        _state.value = _state.value.copy(
            isEmailValid = isEmailValid,
            isPasswordValid = isPasswordValid
        )

        return isEmailValid && isPasswordValid
    }

    fun onRegistrationSnackBarDismissed() {
        viewModelScope.launch {
            _navigateToApp.emit(Unit)
        }
    }
}