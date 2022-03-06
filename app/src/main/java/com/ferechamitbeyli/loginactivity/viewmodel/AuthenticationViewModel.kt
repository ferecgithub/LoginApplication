package com.ferechamitbeyli.loginactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferechamitbeyli.loginactivity.usecase.FetchCurrentUserFromCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val fetchCurrentUserFromCacheUseCase: FetchCurrentUserFromCacheUseCase
) : ViewModel() {

    private val _logOut = MutableSharedFlow<Unit>()
    val logOut = _logOut.asSharedFlow()

    init {
        viewModelScope.launch {
            fetchCurrentUserFromCacheUseCase().onEach { user ->
                if (user == null) {
                    _logOut.emit(Unit)
                }
            }.launchIn(this)
        }
    }
}