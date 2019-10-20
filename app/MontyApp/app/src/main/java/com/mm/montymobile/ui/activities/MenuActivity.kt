package com.mm.montymobile.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.mm.montymobile.R
import com.mm.montymobile.ui.fragments.EditProfileFragment

class MenuActivity : AppCompatActivity() {

    var fragmentName = ""
    var user_number = ""
    var user_name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val intent = intent
        if (intent != null) {
            if (intent.hasExtra("fragmentName")) {
                fragmentName = intent.getStringExtra("fragmentName")

            }

            if (intent.hasExtra("user_number"))
            {
                user_number = intent.getStringExtra("user_number")
            }
            if (intent.hasExtra("user_name"))
            {
                user_name = intent.getStringExtra("user_name")
            }
        }

        Log.e("MenuActivity" , "fragmentName = $fragmentName")

        if (fragmentName == EditProfileFragment::class.java.name){
            loadFragment(EditProfileFragment(), EditProfileFragment::class.java.name)
        }
    }


    private fun loadFragment(fragment: Fragment, name: String) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()
        bundle.putString("user_number" , user_number)
        bundle.putString("user_name" , user_name)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameLayout, fragment  , name).addToBackStack(null)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        val intent = Intent(this  , Dashboard::class.java)

        startActivity(intent)
        finish()
    }
}
