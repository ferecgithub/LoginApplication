package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto
import com.ferechamitbeyli.loginactivity.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LoginUserUseCaseTest {

    private lateinit var getUserByEmailUseCase: GetUserByEmailUseCase
    private lateinit var addLoggedInEmailToCacheUseCase: AddLoggedInEmailToCacheUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun setupUseCase(
        getUserByEmailUseCase: GetUserByEmailUseCase = mock(GetUserByEmailUseCase::class.java),
        addLoggedInEmailToCacheUseCase: AddLoggedInEmailToCacheUseCase = mock(
            AddLoggedInEmailToCacheUseCase::class.java
        )
    ): LoginUserUseCase {
        this.getUserByEmailUseCase = getUserByEmailUseCase
        this.addLoggedInEmailToCacheUseCase = addLoggedInEmailToCacheUseCase

        return LoginUserUseCase(this.getUserByEmailUseCase, this.addLoggedInEmailToCacheUseCase)
    }

    @Test
    fun userNotFoundByEmail_givesFailure() {
        val email = "email"
        val password = "password"

        val useCase = setupUseCase(
            getUserByEmailUseCase = mock(GetUserByEmailUseCase::class.java) {
                throw Exception("User not found")
            }
        )

        runBlocking {
            val result = useCase(email, password)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            assertEquals(Result.Failure, result)
        }
    }

    @Test
    fun passwordsDoNotMatch_givesFailure() {
        val email = "email"
        val password = "real_password"
        val inputPassword = "input_password"

        val useCase = setupUseCase(getUserByEmailUseCase = mock(GetUserByEmailUseCase::class.java) {
            UserDto(0, email, password)
        })

        runBlocking {
            val result = useCase(email, inputPassword)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            assertEquals(Result.Failure, result)
        }
    }

    @Test
    fun withValidInputs_givesSuccess() {
        val email = "email"
        val inputPassword = "input_password"

        val useCase = setupUseCase(
            getUserByEmailUseCase = mock(GetUserByEmailUseCase::class.java) {
                UserDto(0, email, inputPassword)
            }
        )

        runBlocking {
            val result = useCase(email, inputPassword)
            verify(getUserByEmailUseCase, atLeastOnce()).invoke(email)
            verify(addLoggedInEmailToCacheUseCase, atLeastOnce()).invoke(email)
            assertEquals(Result.Success, result)
        }
    }
}