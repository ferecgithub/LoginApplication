package com.ferechamitbeyli.loginactivity.viewmodel

import app.cash.turbine.test
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginErrorState
import com.ferechamitbeyli.loginactivity.ui.auth.states.LoginState
import com.ferechamitbeyli.loginactivity.usecase.GetForgottenPasswordUseCase
import com.ferechamitbeyli.loginactivity.usecase.LoginUserUseCase
import com.ferechamitbeyli.loginactivity.usecase.RegisterUserUseCase
import com.ferechamitbeyli.loginactivity.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginUserUseCase: LoginUserUseCase
    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var getForgottenPasswordUseCase: GetForgottenPasswordUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun setupViewModel(
        loginUserUseCase: LoginUserUseCase = mock(LoginUserUseCase::class.java),
        registerUserUseCase: RegisterUserUseCase = mock(RegisterUserUseCase::class.java),
        getForgottenPasswordUseCase: GetForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java)
    ): LoginViewModel {
        this.loginUserUseCase = loginUserUseCase
        this.registerUserUseCase = registerUserUseCase
        this.getForgottenPasswordUseCase = getForgottenPasswordUseCase

        return LoginViewModel(
            loginUserUseCase,
            registerUserUseCase,
            getForgottenPasswordUseCase
        )
    }

    @Test
    fun loginWithInvalidInputs_givesError() {
        val viewModel = setupViewModel()
        val invalidEmail = ""
        val invalidPassword = ""

        runBlocking {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.login(invalidEmail, invalidPassword)
                verify(loginUserUseCase, never()).invoke(invalidEmail, invalidPassword)
                assertEquals(LoginState(isEmailValid = false, isPasswordValid = false), awaitItem())
            }
        }
    }

    @Test
    fun loginWithValidInputs_givesError() {
        val viewModel = setupViewModel(
            loginUserUseCase = mock(LoginUserUseCase::class.java) {
                Result.Failure
            }
        )

        val email = "ferechamitbeyli@gmail.com"
        val password = "qwerty123"

        runBlocking {
            viewModel.error.test {
                viewModel.login(email, password)
                verify(loginUserUseCase, atLeastOnce()).invoke(email, password)
                assertEquals(LoginErrorState.LOGIN, awaitItem())
            }
        }
    }

    @Test
    fun loginWithValidInputs_givesSuccess() {
        val viewModel = setupViewModel(
            loginUserUseCase = mock(LoginUserUseCase::class.java) {
                Result.Success
            }
        )

        val invalidEmail = ""
        val invalidPassword = ""

        val email = "ferechamitbeyli@gmail.com"
        val password = "qwerty123"

        runBlocking {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.login(invalidEmail, invalidPassword)
                assertEquals(LoginState(isEmailValid = false, isPasswordValid = false), awaitItem())
                viewModel.login(email, password)
                assertEquals(LoginState(isEmailValid = true, isPasswordValid = true), awaitItem())
            }

            viewModel.error.test {
                viewModel.login(email, password)
                expectNoEvents()
            }

            viewModel.navigateToApp.test {
                viewModel.login(email, password)
                assertEquals(Unit, awaitItem())
            }

            verify(loginUserUseCase, atLeast(3)).invoke(email, password)
        }
    }

    @Test
    fun signUpWithValidInputs_givesError() {
        val viewModel = setupViewModel(
            registerUserUseCase = mock(RegisterUserUseCase::class.java) {
                Result.Failure
            }
        )

        val email = "ferechamitbeyli@gmail.com"
        val password = "qwerty123"

        runBlocking {
            viewModel.error.test {
                viewModel.signUp(email, password)
                verify(registerUserUseCase, atLeastOnce()).invoke(email, password)
                assertEquals(LoginErrorState.SIGNUP, awaitItem())
            }
        }
    }

    @Test
    fun signUpWithValidInputs_givesSuccess() {
        val viewModel = setupViewModel(
            loginUserUseCase = mock(LoginUserUseCase::class.java) {
                Result.Success
            },
            registerUserUseCase = mock(RegisterUserUseCase::class.java) {
                Result.Success
            }
        )

        val invalidEmail = ""
        val invalidPassword = ""

        val email = "ferechamitbeyli@gmail.com"
        val password = "qwerty123"

        runBlocking {
            viewModel.state.test {
                assertEquals(LoginState(), awaitItem())
                viewModel.signUp(invalidEmail, invalidPassword)
                assertEquals(LoginState(isEmailValid = false, isPasswordValid = false), awaitItem())
                viewModel.signUp(email, password)
                assertEquals(LoginState(isEmailValid = true, isPasswordValid = true), awaitItem())
            }

            viewModel.error.test {
                viewModel.signUp(email, password)
                expectNoEvents()
            }

            viewModel.registerSuccess.test {
                viewModel.signUp(email, password)
                assertEquals(Unit, awaitItem())
            }

            verify(registerUserUseCase, atLeast(3)).invoke(email, password)
            verify(loginUserUseCase, atLeast(3)).invoke(email, password)
        }
    }

    @Test
    fun restorePasswordInitiated() {
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.bottomSheetShow.test {
                viewModel.restorePasswordInitiated()
                assertEquals(Unit, awaitItem())
            }
        }
    }

    @Test
    fun restorePasswordWithValidEmail_givesSuccess() {
        val email = "ferechamitbeyli@gmail.com"
        val password = "qwerty123"

        val viewModel = setupViewModel(
            getForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java) {
                GetForgottenPasswordUseCase.Result.Success(password)
            }
        )

        runBlocking {
            viewModel.forgotPassGetUserSuccess.test {
                viewModel.restorePassword(email)
                verify(getForgottenPasswordUseCase, atLeastOnce()).invoke(email)
                assertEquals(password, awaitItem())
            }
        }
    }

    @Test
    fun restorePasswordWithValidEmail_givesFailure() {
        val email = "ferechamitbeyli@gmail.com"

        val viewModel = setupViewModel(
            getForgottenPasswordUseCase = mock(GetForgottenPasswordUseCase::class.java) {
                GetForgottenPasswordUseCase.Result.Failure
            }
        )

        runBlocking {
            viewModel.error.test {
                viewModel.restorePassword(email)
                verify(getForgottenPasswordUseCase, atLeastOnce()).invoke(email)
                assertEquals(LoginErrorState.FORGOT_PASSWORD, awaitItem())
            }
        }
    }

    @Test
    fun onRegistrationSnackBarDismissed() {
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.navigateToApp.test {
                viewModel.onRegistrationSnackBarDismissed()
                assertEquals(Unit, awaitItem())
            }
        }
    }

}