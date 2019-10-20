package com.mm.montymobile.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mm.montymobile.R
import com.mm.montymobile.ui.activities.Dashboard

class LoadingFragment : Fragment() {

    private val SPLASH_TIME_OUT = 3000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            val intent = Intent(context , Dashboard::class.java)
            startActivity(intent)
            activity?.finish()
        }, SPLASH_TIME_OUT.toLong())


    }


}
