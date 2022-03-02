package com.ferechamitbeyli.loginactivity.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val DATASTORE_NAME = "login_datastore"
    private const val DATASTORE_LOGGED_IN_EMAIL = "logged_in_email"

    @JvmField
    val DATASTORE_LOGGED_IN_EMAIL_KEY = stringPreferencesKey(DATASTORE_LOGGED_IN_EMAIL)
}