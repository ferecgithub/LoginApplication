package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto
import com.ferechamitbeyli.loginactivity.utils.Result
import timber.log.Timber
import javax.inject.Inject

class RegisterUserUseCase @Inject  constructor(
    private val addUserToDatabaseUseCase: AddUserToDatabaseUseCase,
    private val checkIfUserExistsUseCase: CheckIfUserExistsUseCase
){
    suspend operator fun invoke(email: String, password: String) : Result {
        Timber.d("invoke: $email")
        val isUserExists = checkIfUserExistsUseCase.invoke(email)
        return if (!isUserExists) {
            addUserToDatabaseUseCase.invoke(UserDto(email = email, password = password))
            Timber.d("RegisterUserUseCase : Success")
            Result.Success
        } else {
            Timber.d("RegisterUserUseCase : Failure")
            Result.Failure
        }
    }
}