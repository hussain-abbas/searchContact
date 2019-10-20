package com.mm.montymobile.ui.fragments


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.mm.montymobile.R
import com.mm.montymobile.helper.AppPreferences
import com.mm.montymobile.ui.activities.Dashboard


class SplashFragment : Fragment() {

    private var appPreferences: AppPreferences? = null
    private val SPLASH_TIME_OUT = 3000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appPreferences = AppPreferences(context!!)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var number = appPreferences?.getStringAuth(AppPreferences.NUMBER)  //TODO number is store in the app preferences to check if someone logged in or not
        Handler().postDelayed({
            if (number.isNullOrEmpty()) //TODO if number not saved load register Fragment
            {

                loadFragment(RegisterFragment(), RegisterFragment::class.java.name)
            }
            else { //TODO else goto dashboard
                val intent = Intent(context , Dashboard::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }, SPLASH_TIME_OUT.toLong())
    }



    private fun loadFragment(fragment: Fragment, name: String) {
        val fm = activity?.supportFragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, fragment  , name)?.addToBackStack(null)
        fragmentTransaction?.commit()

    }

}
