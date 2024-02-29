package com.marwadtech.userapp.ui.bottomNavigation

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseActivity
import com.marwadtech.userapp.databinding.ActivityUserDashboardBinding
import com.marwadtech.userapp.ui.bottomNavigationDrawer.BottomNavigationDrawerActivity
import com.marwadtech.userapp.ui.navigationDrawer.NavigationDrawerActivity
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDashboardBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.my_nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigate_home,
                R.id.navigate_profile,
                R.id.navigate_notification
            )
        )

        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.navigate_home -> {
                    setToolbar(
                        isVisible = true,
                        isCenter = true
                    )
                }


                else -> {

                }

            }

        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_menu -> {
                }
            }
            return@setOnMenuItemClickListener true
        }

    }

    private fun setToolbar(
        isVisible: Boolean = true,
        isCenter: Boolean = true,
        titleColor: Int = R.color.colorPrimary
    ) {
        val drawable: Drawable? = binding.toolbar.navigationIcon
        binding.toolbar.isTitleCentered = isCenter
        binding.toolbar.setTitleTextColor(
            AppCompatResources.getColorStateList(
                this,
                titleColor
            )
        )
        if (isVisible) {
            binding.toolbar.visible()
        } else {
            binding.toolbar.gone()
        }
    }

    companion object {
        private val TAG = UserDashboardActivity::class.java.name
    }
}