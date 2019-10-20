package com.mm.montymobile.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mm.montymobile.R
import com.mm.montymobile.helper.AppPreferences
import com.mm.montymobile.ui.activities.Dashboard
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*


class EditProfileFragment : Fragment() {

    private var appPreferences: AppPreferences? = null
    var user_number = ""
    var user_name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        appPreferences = AppPreferences(context!!)
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pn = appPreferences?.getStringAuth(AppPreferences.NUMBER)

        val bundle = arguments
        if (bundle != null)
        {
            user_number = bundle.getString("user_number")
            user_name = bundle.getString("user_name")
        }

        if (!user_name.isNullOrEmpty())
        {
            view.txtName.text = user_name
        }
        if (!user_number.isNullOrEmpty())
        {
            view.number.text = user_number
        }
        else {
            view.number.text = pn
        }
        view.back.setOnClickListener {
            startActivity(Intent(context , Dashboard::class.java))
            activity?.finish()
        }
    }


}
