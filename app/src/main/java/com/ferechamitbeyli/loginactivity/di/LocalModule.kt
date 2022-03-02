package com.ferechamitbeyli.loginactivity.di

import android.content.Context
import androidx.room.Room
import com.ferechamitbeyli.loginactivity.model.db.LoginDatabase
import com.ferechamitbeyli.loginactivity.model.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, LoginDatabase::class.java, "login_app.db").build()

    @Provides
    @Singleton
    fun provideUserDao(
        loginDatabase: LoginDatabase
    ): UserDao = loginDatabase.userDao()
}