package com.maklumi.cats.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.maklumi.cats.R
import com.maklumi.cats.util.ID_PERMOHONAN_SMS

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHost
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController, null)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun periksaKebenaranUntukSMS() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder(this)
                    .setTitle("Permintaan izin untuk hantar SMS")
                    .setMessage("Jika mahu menghantar SMS, sila klik OK")
                    .setPositiveButton("OK") { _, _ -> mintaPermohonanSMS() }
                    .setNegativeButton("Tak JADI") { _, _ -> bagitauDetailFragment(false) }
                    .show()
            } else {
                mintaPermohonanSMS()
            }
        } else {
            bagitauDetailFragment(lulus = true)
        }
    }

    private fun mintaPermohonanSMS() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.SEND_SMS),
            ID_PERMOHONAN_SMS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ID_PERMOHONAN_SMS -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    bagitauDetailFragment(true)
                } else {
                    bagitauDetailFragment(false)
                }
            }
        }
    }

    private fun bagitauDetailFragment(lulus: Boolean) {
        val fragmentYangPanggil =
            supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.last()
        if (fragmentYangPanggil is DetailFragment) {
            fragmentYangPanggil.bilaKeizinanDiterima(lulus)
        }
    }
}