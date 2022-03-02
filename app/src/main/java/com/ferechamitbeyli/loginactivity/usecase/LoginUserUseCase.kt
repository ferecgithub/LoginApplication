package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.domain.User
import com.ferechamitbeyli.loginactivity.model.db.dao.UserDao
import javax.inject.Inject
import kotlin.Exception

class LoginUserUseCase @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(email: String, password: String): Result {
        return try {
            val userDto = userDao.findUserByEmail(email)
            if (userDto.password != password) {
                throw IllegalArgumentException("Passwords do not match!")
            }
            Result.Success(User(userDto.email))
        } catch (e: IllegalArgumentException) {
            Result.Failure(FailureReason.INVALID_PASSWORD)
        } catch (e: Exception) {
            Result.Failure(FailureReason.USER_NOT_FOUND)
        }
    }

    sealed class Result {
        data class Success(val user: User) : Result()
        data class Failure(val failureReason: FailureReason) : Result()
    }

    enum class FailureReason {
        INVALID_PASSWORD,
        USER_NOT_FOUND
    }
}