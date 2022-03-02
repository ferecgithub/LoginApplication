package com.ferechamitbeyli.loginactivity.model.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ferechamitbeyli.loginactivity.utils.Constants.DATASTORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun addToCache(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { data ->
            data[key] = value
        }
    }
}