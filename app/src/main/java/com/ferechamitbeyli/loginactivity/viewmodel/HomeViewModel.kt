package com.ferechamitbeyli.loginactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.loginactivity.usecase.FetchCurrentUserFromCacheUseCase
import com.ferechamitbeyli.loginactivity.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val fetchCurrentUserFromCacheUseCase: FetchCurrentUserFromCacheUseCase
) : ViewModel() {

    private val _currentUserEmail = MutableStateFlow("")
    val currentUserEmail = _currentUserEmail.asStateFlow()

    init {
        viewModelScope.launch {
            fetchCurrentUserFromCacheUseCase().onEach { user ->
                Timber.d("Current User : ${user.toString()}")
                if (user != null) {
                    _currentUserEmail.emit(user.email)
                }
            }.launchIn(this)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase()
            Timber.d("Logout invoked in viewModel")
        }
    }
}