package com.mm.montymobile.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mm.montymobile.R
import com.mm.montymobile.ui.fragments.SplashFragment

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestMultiPermission() //TODO request permissions like contact
        loadFragment(SplashFragment() , SplashFragment::class.java.name) // TODO Load Fragment
    }

    private fun loadFragment(fragment: Fragment, name: String) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment  , name).addToBackStack(null)
        fragmentTransaction.commit()

    }

    private fun requestMultiPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_CONTACTS
                )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { error -> Toast.makeText(applicationContext, "Error occurred! $error", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }

    private fun showSettingsDialog() {  //TODO this is used when user doesn't allow any permission
        try {
            val builder = AlertDialog.Builder(this@SplashScreen , R.style.MyDialogTheme22)
            builder.setTitle("Need Permissions")
            //builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
                dialog.cancel()
                openSettings()
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun openSettings() {  //TODO this is used to open settings
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
