package com.marwadtech.userapp.ui.bottomNavigationDrawer

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.marwadtech.localfinds.internet.ConnectivityReceiver
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseActivity
import com.marwadtech.userapp.databinding.ActivityBottomNavigationDrawerBinding
import com.marwadtech.userapp.databinding.ActivityNavigationDrawerBinding
import com.marwadtech.userapp.databinding.ActivityUserDashboardBinding
import com.marwadtech.userapp.ui.bottomNavigation.UserDashboardActivity
import com.marwadtech.userapp.ui.navigationDrawer.NavigationDrawerActivity
import com.marwadtech.userapp.utils.SharedViewModel
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.logout
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visible
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavigationDrawerActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityBottomNavigationDrawerBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var appUpdateManager: AppUpdateManager
    private var updateInfo: AppUpdateInfo? = null
    private val channelId = "channelId"
    private lateinit var notificationManager: NotificationManager
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        binding = ActivityBottomNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
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
                }
            }
            return@setOnMenuItemClickListener true
        }
        checkUpdates()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
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
            R.id.logout->{
                logout(this@BottomNavigationDrawerActivity,spUtils)
                true
            }
            else -> {
                true
            }
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            }
        }

    private fun checkUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(this@BottomNavigationDrawerActivity)
        // appUpdateManager.registerListener(updateListener)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                updateInfo = appUpdateInfo
                updateInfo?.apply {
                    startForInAppUpdate(this)
                }
            }
        }
    }
    private fun startForInAppUpdate(updateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(updateInfo, AppUpdateType.IMMEDIATE, this, 1101)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1101) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                checkUpdates()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    val updateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            Log.e(TAG, "InstallStateUpdatedListener : $bytesDownloaded/$totalBytesToDownload")
        }

        if (state.installStatus() == InstallStatus.DOWNLOADED) {

            popupSnackbarForCompleteUpdate()
        }
    }

    fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.lytParent),
            "an update has just been downloaded",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.restart)) { appUpdateManager.completeUpdate() }
            setActionTextColor(
                AppCompatResources.getColorStateList(
                    this@BottomNavigationDrawerActivity,
                    R.color.white
                )
            )
            show()
        }
    }
    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = connectivityReceiverListener
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                updateInfo = appUpdateInfo
                updateInfo?.apply {
                    startForInAppUpdate(this)
                }
            }
        }
    }

    override fun onPause() {
        try {
            this.unregisterReceiver(ConnectivityReceiver())
            //            appUpdateManager.unregisterListener(updateListener)
        } catch (e: IllegalArgumentException) {
        }
        super.onPause()
    }

    private val connectivityReceiverListener =
        object : ConnectivityReceiver.ConnectivityReceiverListener {
            override fun onNetworkConnectionChanged(isConnected: Boolean) {
                Log.e(TAG, "onNetworkConnectionChanged: $isConnected")
                if (!isConnected) {
                    navController.navigate(R.id.action_global_navGraphInternet)
                } else {
                    val fragmentName = navController.currentDestination?.displayName
                    Log.e(TAG, "onNetworkConnectionChanged: $fragmentName")
                    if (fragmentName?.contains("internetConnectionFragment") == true) {
                        navController.navigateUp()
                    }
                }
            }
        }
}