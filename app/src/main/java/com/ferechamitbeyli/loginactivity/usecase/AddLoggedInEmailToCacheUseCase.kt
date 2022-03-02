package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.cache.DataStoreManager
import com.ferechamitbeyli.loginactivity.utils.Constants.DATASTORE_LOGGED_IN_EMAIL_KEY
import timber.log.Timber
import javax.inject.Inject

class AddLoggedInEmailToCacheUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    suspend operator fun invoke(email: String) {
        Timber.d("invoke: $email")
        dataStoreManager.addToCache(DATASTORE_LOGGED_IN_EMAIL_KEY, email)
    }
}