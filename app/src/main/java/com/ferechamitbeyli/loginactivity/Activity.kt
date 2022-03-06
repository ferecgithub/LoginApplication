package com.ferechamitbeyli.loginactivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.ferechamitbeyli.loginactivity.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class Activity : AppCompatActivity() {

    private val viewModel: AuthenticationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        lifecycleScope.launch {
            viewModel.logOut.onEach {
                Timber.d("Logging out!")
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView_activity_main) as NavHostFragment
                navHostFragment.navController.navigate(R.id.logOut)
            }.launchIn(this)
        }

    }

}