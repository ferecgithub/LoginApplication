package com.ferechamitbeyli.loginactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.loginactivity.usecase.CheckIfUserExistsUseCase
import com.ferechamitbeyli.loginactivity.usecase.GetUserByEmailUseCase
import com.ferechamitbeyli.loginactivity.usecase.LoginUserUseCase
import com.ferechamitbeyli.loginactivity.usecase.RegisterUserUseCase
import com.ferechamitbeyli.loginactivity.utils.Result
import com.ferechamitbeyli.loginactivity.utils.isValidEmail
import com.ferechamitbeyli.loginactivity.utils.isValidPassword
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginState
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
    private val checkIfUserExistsUseCase: CheckIfUserExistsUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
) : ViewModel() {

    private val _bottomSheetShow = MutableSharedFlow<Unit>()
    val bottomSheetShow = _bottomSheetShow.asSharedFlow()

    private val _forgotPassGetUserSuccess = MutableSharedFlow<String>()
    val forgotPassGetUserSuccess = _forgotPassGetUserSuccess.asSharedFlow()

    private val _registerSuccess = MutableSharedFlow<Unit>()
    val registerSuccess = _registerSuccess.asSharedFlow()

    private val _navigateToApp = MutableSharedFlow<Unit>()
    val navigateToApp = _navigateToApp.asSharedFlow()

    private val _error = MutableSharedFlow<Unit>()
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
                        _error.emit(Unit)
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
                        _error.emit(Unit)
                    }
                    Result.Success -> _registerSuccess.emit(Unit)
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
            try {
                if (checkIfUserExistsUseCase(email)) {
                    val userDto = getUserByEmailUseCase(email)
                    _forgotPassGetUserSuccess.emit(userDto.password)
                }
            } catch (e: Exception) {
                Timber.d("Error in restorePassword(), ${e.message}")
                _error.emit(Unit)
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
}