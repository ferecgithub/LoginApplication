package com.ferechamitbeyli.loginactivity.utils

import android.content.Context
import android.util.Patterns
import androidx.annotation.AttrRes
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.withStyledAttributes

fun String.isValidEmail(): Boolean = !isEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = !isEmpty() && this.length > 7

fun Context.getColorByAttribute(@AttrRes attr: Int): Int {
    withStyledAttributes(0, intArrayOf(attr)) {
        return getColorOrThrow(0)
    }
    throw IllegalArgumentException()
}