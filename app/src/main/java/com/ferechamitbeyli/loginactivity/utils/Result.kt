package com.ferechamitbeyli.loginactivity.utils


sealed class Result{
    object Success: Result()
    object Failure: Result()
}
