package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.db.dao.UserDao
import timber.log.Timber
import javax.inject.Inject

class CheckIfUserExistsUseCase @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(email: String): Boolean {
        Timber.d("invoke!")
        return userDao.checkIfUserExists(email)
    }
}