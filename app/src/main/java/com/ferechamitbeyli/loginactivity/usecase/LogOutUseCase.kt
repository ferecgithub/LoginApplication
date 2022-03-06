package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.cache.DataStoreManager
import com.ferechamitbeyli.loginactivity.utils.Constants.DATASTORE_LOGGED_IN_EMAIL_KEY
import timber.log.Timber
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke() {
        Timber.d("Logging out by removing cache")
        dataStoreManager.removeFromDataStore(DATASTORE_LOGGED_IN_EMAIL_KEY)
    }
}