package com.marwadtech.userapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.marwadtech.userapp.databinding.ActivitySplashBinding
import com.marwadtech.userapp.ui.auth.AuthActivity
import com.marwadtech.userapp.ui.user.UserDashboardActivity
import com.marwadtech.userapp.utils.SpUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    @Inject lateinit var spUtils: SpUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the splash screen transition.
//        val splashScreen = installSplashScreen()
//
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if(spUtils.hasFirstTime){
//            goNextScreenIntro()
//            return
//        }
        if (spUtils.isLoggedIn){
            goNextScreenDashboard()
        }else{
            goNextScreenAuth()
        }

    }

    private fun goNextScreenDashboard() {
        lifecycleScope.launch {
            delay(2000)
            goDashBoard()
        }
    }

    private fun goNextScreenAuth() {
        lifecycleScope.launch {
            delay(2000)
            goAuthActivity()
        }
    }

    private fun goNextScreenIntro() {
        lifecycleScope.launch {
            delay(2000)
//            goIntroActivity()
        }
    }

    private fun goAuthActivity() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun goDashBoard() {
        val intent = Intent(this@SplashActivity, UserDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = SplashActivity::class.java.name
    }
}
