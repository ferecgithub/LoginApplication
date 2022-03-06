package com.ferechamitbeyli.loginactivity.usecase

import com.ferechamitbeyli.loginactivity.model.cache.DataStoreManager
import com.ferechamitbeyli.loginactivity.model.domain.User
import com.ferechamitbeyli.loginactivity.utils.Constants.DATASTORE_LOGGED_IN_EMAIL_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchCurrentUserFromCacheUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    suspend operator fun invoke(): Flow<User?> {
        return dataStoreManager.fetchKeyValue(DATASTORE_LOGGED_IN_EMAIL_KEY).map {
            if (it != null) {
                User(it)
            } else {
                null
            }
        }
    }
}