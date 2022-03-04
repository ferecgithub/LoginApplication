package com.ferechamitbeyli.loginactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class Activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity)


    }

}