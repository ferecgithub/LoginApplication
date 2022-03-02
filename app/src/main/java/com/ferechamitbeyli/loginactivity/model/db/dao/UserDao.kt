package com.ferechamitbeyli.loginactivity.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email=:email")
    fun findUserByEmail(email: String): UserDto

    @Query("SELECT EXISTS(SELECT * FROM users WHERE email=:email)")
    fun checkIfUserExists(email: String): Boolean

    @Insert
    fun insert(user: UserDto)
}