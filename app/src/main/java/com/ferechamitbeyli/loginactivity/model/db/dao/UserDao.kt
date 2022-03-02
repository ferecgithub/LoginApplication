package com.ferechamitbeyli.loginactivity.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email=:email")
    fun findUserByEmail(email: String): UserDto
}