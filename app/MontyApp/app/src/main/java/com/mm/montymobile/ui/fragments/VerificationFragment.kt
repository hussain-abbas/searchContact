package com.mm.montymobile.ui.fragments


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alert.sweetalert.SweetAlertDialog
import com.google.gson.Gson

import com.mm.montymobile.R
import com.mm.montymobile.helper.AppPreferences
import com.mm.montymobile.helper.api.ApiClient
import com.mm.montymobile.helper.api.ApiResponse
import com.mm.montymobile.helper.pojo.AllContactsResponse
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse
import com.mm.montymobile.helper.pojo.RegisterUserResponse
import com.mm.montymobile.ui.activities.Dashboard
import com.mm.montymobile.viewModel.AppViewModel
import com.squareup.okhttp.OkHttpClient
import kotlinx.android.synthetic.main.fragment_verification.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class VerificationFragment : Fragment() {

    var TAG = VerificationFragment::class.java.name
    var isTimeCompleted = false
    var countDownTimer: CountDownTimer? = null
    var code = ""
    var number = ""
    private var viewModel: AppViewModel? = null


    private var appPreferences:AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
        appPreferences = AppPreferences(context!!)
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null)
        {
            if (bundle.getString("code") != null)
            {
                code = bundle.getString("code")
                number = bundle.getString("number")

            }
        }

        d1.addTextChangedListener(mTextWatcherVerificationD1)
        d2.addTextChangedListener(mTextWatcherVerificationD2)
        d3.addTextChangedListener(mTextWatcherVerificationD3)
        d4.addTextChangedListener(mTextWatcherVerificationD4)

        if (!isTimeCompleted)
        {
            startCountDown() //TODO start countdown
        }

        next.setOnClickListener {
            val n1 = d1.text.toString()
            val n2 = d2.text.toString()
            val n3 = d3.text.toString()
            val n4 = d4.text.toString()

            val number = n1 + n2 + n3 + n4
            if (number.length != 4)
            {
                Toast.makeText(context, "Invalid Verification code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (number != code)
            {

                Toast.makeText(context, "Please enter a valid code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser() //TODO Register user on server
           // appPreferences?.putStringAuth(AppPreferences.NUMBER , number)

        }

        time.setOnClickListener {
            if (isTimeCompleted)
            {
                startCountDown() //TODO start timer for new code
                getVerificationCode(number)//TODO if time is completed and user doesnt get a verification code he/she can get another code
                //startPhoneNumberVerification(number!!)
            }
        }
        back.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }
    }


    private fun startCountDown() //TODO start the count
    {
        //TODO 15 second count after 15 seconds user can resend the verification code
          countDownTimer = object : CountDownTimer(15000 , 1000){
            override fun onFinish() {
                Log.e("startCountDown", "finished")
                isTimeCompleted = true
                time?.text = "Resend Sms"
                txtTime?.visibility = View.GONE
            }

            override fun onTick(millisUntilFinished: Long) {
                txtTime?.visibility = View.VISIBLE
                Log.d("startCountDown", millisUntilFinished.toString())
                val t = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
                time?.text  = t
            }

        }
        countDownTimer?.start()
    }


    private val mTextWatcherVerificationD1 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val d1 = d1!!.text.toString()

            if (d1.isNotEmpty())
            {
                d2.requestFocus()
            }
        }

    }
    private val mTextWatcherVerificationD2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val d = d2!!.text.toString()

            if (d.isNotEmpty())
            {
                d3.requestFocus()
            }
        }

    }
    private val mTextWatcherVerificationD3 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val d1 = d3!!.text.toString()

            if (d1.isNotEmpty() )
            {
                d4.requestFocus()
            }
        }

    }

    private val mTextWatcherVerificationD4 = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val d = d3!!.text.toString()

            if (d.isNotEmpty()|| d.length > 0)
            {

                val c1 =   d1.text.toString()
                val c2 =   d2.text.toString()
                val c3 =   d3.text.toString()
                val c4 =   d4.text.toString()

                val vc = c1 + c2 + c3 + c4

                Log.e(TAG, "code = $vc")
                if (vc.length < 3)
                {
                    Toast.makeText(context, "Please enter a valid code", Toast.LENGTH_SHORT).show()
                }
                else {

                    if (vc != code)
                    {

                        Toast.makeText(context, "Please enter a valid code", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        registerUser()
                    }
                }


            }

        }

        override fun afterTextChanged(s: Editable) {

            //Toast.makeText(SignupActivity.this, "s = "+s, Toast.LENGTH_SHORT).show();
        }
    }

    private fun registerUser()
    {
        val jsonObject = JSONObject()
        jsonObject.put("number" , number)
        jsonObject.put("name" , "")

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, jsonObject.toString())

        verifyCode(body)
    }
    private fun verifyCode(body: RequestBody )
    {
        observerVerificationCode(viewModel!!.registerUser(body) )
    }

    private fun observerVerificationCode(liveData: LiveData<RegisterUserResponse> )
    {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()
        liveData.observe(this , Observer { data ->
            val response = data?.response

            when (response) {
                "200"  -> {
                    if (pDialog.isShowing)
                    {
                        pDialog.dismissWithAnimation()
                    }
                    countDownTimer?.cancel()
                    appPreferences?.putStringAuth(AppPreferences.NUMBER , number)
                    loadFragment(LoadingFragment(), LoadingFragment::class.java.name)

                }

                "403"  -> {
                    if (pDialog.isShowing)
                    {
                        pDialog.dismissWithAnimation()
                    }
                    countDownTimer?.cancel()
                    appPreferences?.putStringAuth(AppPreferences.NUMBER , number)
                    loadFragment(LoadingFragment(), LoadingFragment::class.java.name)

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

        getCode(body )


    }

    private fun getCode(body: RequestBody )
    {
        observerGetVerificationCode(viewModel!!.getVerificationCode(body) )
    }

    private fun observerGetVerificationCode(liveData: LiveData<MobileVerificationCodeResponse>  )
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
    private fun loadFragment(fragment: Fragment, name: String) {
        val fm = activity?.supportFragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        fragmentTransaction?.replace(R.id.frameLayout, fragment  , name)?.addToBackStack(null)
        fragmentTransaction?.commit()

    }
}
