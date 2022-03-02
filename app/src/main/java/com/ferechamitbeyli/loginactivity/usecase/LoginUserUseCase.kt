package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.utils.Result
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

class LoginUserUseCase @Inject constructor(
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val addLoggedInEmailToCacheUseCase: AddLoggedInEmailToCacheUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result {
        Timber.d("invoke: $email")
        return try {
            val userDto = getUserByEmailUseCase(email)
            if (userDto.password != password) {
                Timber.d("LoginUserUseCase : Failed, passwords do not matched.")
                Result.Failure
            }
            addLoggedInEmailToCacheUseCase(email)
            Result.Success
        } catch (e: Exception) {
            Timber.d("LoginUserUseCase : Failed, Exception : ${e.message}")
            Result.Failure
        }
    }
}