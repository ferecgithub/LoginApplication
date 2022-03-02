package com.ferechamitbeyli.loginactivity.model.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDto(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val email: String,
    val password: String
)
