package com.marwadtech.userapp.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marwadtech.userapp.R

class UserDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
    }
    companion object {
        private val TAG = UserDashboardActivity::class.java.name
    }
}