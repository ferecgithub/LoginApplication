package com.ferechamitbeyli.loginactivity.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ferechamitbeyli.loginactivity.model.db.dto.UserDto
import com.ferechamitbeyli.loginactivity.model.db.dao.UserDao

@Database(entities = [UserDto::class], version = 1)
abstract class LoginDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
}