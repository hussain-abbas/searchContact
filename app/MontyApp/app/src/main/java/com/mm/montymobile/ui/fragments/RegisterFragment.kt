package com.mm.montymobile.ui.fragments


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alert.sweetalert.SweetAlertDialog
import com.google.gson.Gson

import com.mm.montymobile.R
import com.mm.montymobile.helper.AppPreferences
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

import java.io.IOException

import kotlin.random.Random
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.Snackbar
import com.mm.montymobile.helper.api.ApiClientVerification
import com.mm.montymobile.helper.api.ApiResponse
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse
import com.mm.montymobile.viewModel.AppViewModel
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap


class RegisterFragment : Fragment() {

    private val RC_HINT = 2
    private var appPreferences: AppPreferences? = null
    private var viewModel: AppViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        appPreferences = AppPreferences(context!!)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConfirm.setOnClickListener {
            val number = edNumber.text.toString()

            if (TextUtils.isEmpty(number))
            {
                edNumber.error = "Required Field"
                return@setOnClickListener
            }

            getVerificationCode(number)
        }

    }



    private fun  getVerificationCode(number: String)
    {
        val code = String.format("%04d", Random.nextInt(10000))
        val jsonObject = JSONObject()

        jsonObject.put("sid" , "930088c7-b5ce-4be6-87af-047e391d1e79")
        jsonObject.put("token" , "56d6a0d1-8ba0-4d31-9af9-1cabc88a5432")
        jsonObject.put("from" , "+923355903285")
        jsonObject.put("to" , number)
        jsonObject.put("message" , "Verification code is: $code")

        Log.e("JSONObject", "JSONObject = $jsonObject")
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, jsonObject.toString())

        getCode(body,code,number)


    }

    private fun getCode(body: RequestBody,code: String,number: String)
    {
        observerGetVerificationCode(viewModel!!.getVerificationCode(body) , code , number)
    }

    private fun observerGetVerificationCode(liveData: LiveData<MobileVerificationCodeResponse> , code:String , number: String)
    {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()
        liveData.observe(this , Observer { data ->
            val response = data?.messageCount

            when (response) {
                1 -> {
                    if (pDialog.isShowing)
                    {
                        pDialog.dismissWithAnimation()
                    }
                    loadFragment(VerificationFragment() , VerificationFragment::class.java.name,code,number)

                }
                0 -> {
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "Failed"
                    pDialog.contentText = "Something went wrong please try again later"
                    pDialog.confirmText = "OK"
                    pDialog.setConfirmClickListener {
                        it.dismissWithAnimation()
                    }
                }
                else -> {
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "Failed"
                    pDialog.contentText = "Unable to connect to server. Please make sure you have an active internet "
                    pDialog.confirmText = "OK"
                    pDialog.setConfirmClickListener {
                        it.dismissWithAnimation()
                    }
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment, name: String , code: String , number: String) {
        val fm = activity?.supportFragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        val bundle = Bundle()
        bundle.putString("code" , code)
        bundle.putString("number" , number)
        fragment.arguments = bundle
        fragmentTransaction?.replace(R.id.frameLayout, fragment  , name)?.addToBackStack(null)
        fragmentTransaction?.commit()

    }


}
