package com.marwadtech.userapp.ui.bottomNavigationDrawer

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.marwadtech.userapp.R
import com.marwadtech.userapp.databinding.ActivityBottomNavigationDrawerBinding
import com.marwadtech.userapp.databinding.ActivityNavigationDrawerBinding
import com.marwadtech.userapp.ui.navigationDrawer.NavigationDrawerActivity
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityBottomNavigationDrawerBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_dashboard)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigate_home,
                R.id.navigate_profile,
                R.id.navigate_notification,
            ),binding.drawerLayout
        )
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavBar, navController)
        binding.navView.setNavigationItemSelectedListener(this)

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
                    val intent = Intent(this, NavigationDrawerActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
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
        private val TAG = BottomNavigationDrawerActivity::class.java.name
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.navigate_home -> {
                Toast.makeText(this,"clicked home", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.navigate_notification -> {
                Toast.makeText(this,"clicked notification", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.navigate_profile -> {
                Toast.makeText(this,"clicked profile", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                true
            }
        }
    }
}