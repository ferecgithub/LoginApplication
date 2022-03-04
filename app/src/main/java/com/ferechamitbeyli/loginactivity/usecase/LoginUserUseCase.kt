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
        try {
            val userDto = getUserByEmailUseCase(email)
            if (userDto.password != password) {
                Timber.e("LoginUserUseCase : Failed, passwords do not matched.")
                return Result.Failure
            }
            addLoggedInEmailToCacheUseCase(email)
            Timber.d("Success!")
            return Result.Success
        } catch (e: Exception) {
            Timber.e("LoginUserUseCase : Failed, Exception : ${e.message}")
            return Result.Failure
        }
    }
}