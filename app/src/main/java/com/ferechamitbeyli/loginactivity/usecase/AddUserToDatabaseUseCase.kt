package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.db.dao.UserDao
import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto
import timber.log.Timber
import javax.inject.Inject

class AddUserToDatabaseUseCase @Inject constructor(
    private val userDao: UserDao
) {
    suspend operator fun invoke(userDto: UserDto) {
        Timber.d("invoke!")
        userDao.insert(userDto)
    }
}