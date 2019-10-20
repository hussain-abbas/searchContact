package com.mm.montymobile.ui.activities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import com.alert.sweetalert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.mm.montymobile.R
import com.mm.montymobile.helper.AppPreferences
import com.mm.montymobile.helper.adapter.SearchAdapter
import com.mm.montymobile.helper.api.ApiClient
import com.mm.montymobile.helper.api.ApiClientVerification
import com.mm.montymobile.helper.api.ApiResponse
import com.mm.montymobile.helper.pojo.AllContactsResponse
import com.mm.montymobile.helper.pojo.ExternalContact
import com.mm.montymobile.helper.pojo.MobileVerificationCodeResponse
import com.mm.montymobile.helper.pojo.SaveContactsResponse
import com.mm.montymobile.ui.fragments.EditProfileFragment
import com.mm.montymobile.ui.fragments.VerificationFragment
import com.mm.montymobile.viewModel.AppViewModel
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.fragment_verification.*
import kotlinx.android.synthetic.main.lay_drawer_menu.view.*
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class Dashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = Dashboard::class.java.name
    private var contactList: MutableList<ExternalContact>? = null
    private var appPreferences: AppPreferences? = null
    private var viewModel: AppViewModel? = null
    private var adapter: SearchAdapter? = null
    private var list: MutableList<AllContactsResponse>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java) //TODO init ViewModel
        appPreferences = AppPreferences(this) //TODO initializing Shared preferences
        contactList = ArrayList<ExternalContact>() as MutableList<ExternalContact>?

        contactList?.clear()
        list = ArrayList<AllContactsResponse>()

        adapter = SearchAdapter( ) //TODO init adapter
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter

        search.addTextChangedListener(mTextWatcher)

        btnSearch.setOnClickListener {
            val number = search.text.toString()
            if (number.isEmpty())
            {
                search.error = "Please enter a number "
                return@setOnClickListener
            }
            searchContact(number)
        }


        adapter?.setOnItemClickListener { //TODO if user click on the number it will go to the view detail screen
            val intent = Intent(this@Dashboard , MenuActivity::class.java)
            intent.putExtra("fragmentName" , EditProfileFragment::class.java.name)
            intent.putExtra("user_number" , it.number)
            intent.putExtra("user_name" , it.name)
            startActivity(intent)
            finish()
        }


        val isContactSaved = appPreferences?.getBooleanAuth(AppPreferences.ISCONTACTEDSAVE) //TODO check if user's contact is already in database then don't need to run this commands
        if (!isContactSaved!!) {
            val listViewContactsLoader = tasktoparsethecontact() //TODO get all contact in phone
            listViewContactsLoader.execute()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.editProfile.setOnClickListener { //TODO user click on navigation view click listener
            drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this@Dashboard , MenuActivity::class.java)
            intent.putExtra("fragmentName" , EditProfileFragment::class.java.name)
            startActivity(intent)
            finish()

        }

        menu.setOnClickListener {//TODO if user click on the humburger icon menu will open
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener(this)
    }
    inner class tasktoparsethecontact() : AsyncTask<Void, Void, String>() {


        var pDialog: SweetAlertDialog =  SweetAlertDialog(this@Dashboard, SweetAlertDialog.PROGRESS_TYPE)
        override fun doInBackground(vararg params: Void?): String? {
            contactList = getContacts(this@Dashboard)

            var size = contactList?.size
            var i = 0
            var newcontact: String
            while (i < size!!) {
                newcontact = contactList?.get(i)!!.number
                contactList?.get(i)!!.number = newcontact
                i++
            }

            return ""
        }


        override fun onPreExecute() {
            super.onPreExecute()
            try {
                pDialog?.progressHelper?.barColor = Color.parseColor("#A5DC86")
                pDialog?.titleText = "Getting Contacts"
                pDialog?.setCancelable(false)
                pDialog?.show()
            }
            catch (e: Exception) {

            }

        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


            Log.e(TAG , "contact size = "+contactList?.size)
            if (contactList!!.size > 0) {
                 filterunique()
            }
            if (pDialog.isShowing)
            {
                pDialog.dismissWithAnimation()
            }
            //session.set_contactfirsttimeloaded(true)
        }

    }


    private fun filterunique()
    {
        if (contactList!!.size > 0) {
            var filterdata = ArrayList<ExternalContact>()
            filterdata.addAll(contactList!!)
            contactList?.clear()
            contactList?.add(filterdata.get(0))
            for (contact: ExternalContact in filterdata) {
                var size = contactList?.size
                var j = 0
                var recordfound = false
                while (j < size!!) {
                    if (contact.number.trim().equals(contactList!!.get(j).number.trim())) {
                        recordfound = true
                    }
                    j++
                }
                if (!recordfound) {
                    contactList?.add(contact)
                }



            }

            if (contactList!!.size > 0 )
            {
                 val gson =   GsonBuilder().create()
                 val myCustomArray = gson.toJsonTree(contactList).asJsonArray

                 Log.e(TAG , "jsonarray: $myCustomArray")

                saveContacts(myCustomArray)
            }

            Log.e(TAG , "contact size = "+contactList?.size)
        }
        else
        {
            val listViewContactsLoader = tasktoparsethecontact()
            listViewContactsLoader.execute()
        }
    }

    private fun saveContacts(jsonArray: JsonArray)
    {

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, jsonArray.toString())

        saveContactsLD(body)

    }

    private fun saveContactsLD(body: RequestBody )
    {
        observerSAveContacts(viewModel!!.saveContacts(body) )
    }

    private fun observerSAveContacts(liveData: LiveData<SaveContactsResponse>)
    {
        val pDialog = SweetAlertDialog(this@Dashboard, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Please wait..."
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
                    appPreferences?.putBooleanAuth(AppPreferences.ISCONTACTEDSAVE , true)

                }
                else -> {
                    pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "Failed"
                    pDialog.contentText = "Something went wrong please try again later!"
                    pDialog.confirmText = "OK"
                    pDialog.setConfirmClickListener {
                        it.dismissWithAnimation()
                    }
                }
            }
        })
    }

    private fun getContacts(ctx: Context): ArrayList<ExternalContact> {
        val list = ArrayList<ExternalContact>()
        val contentResolver = ctx.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            null, null,  ContactsContract.Contacts.DISPLAY_NAME + " ASC")
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null
                    )
                    val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                        ctx.contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,id.toLong())
                    )

                    val person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                    val pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)

                    var tempno:String
                    var photo: Bitmap? = null
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream)
                    }
                    while (cursorInfo!!.moveToNext()) {

                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val number = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val contact = ExternalContact(name , number , "" , " " , "" , "")

                         list.add(contact)
                    }

                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        return list
    }

    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val text = search!!.text.toString()

            if (text.isNotEmpty() || text.length > 0)
            {
                recyclerView?.visibility = View.VISIBLE


            }
            else
            {
                recyclerView?.visibility = View.GONE
            }
        }

    }

    private fun searchContact(query:String)
    {
        observerSearchContact(viewModel!!.searchContacts(query) )

    }
    private fun observerSearchContact(liveData: LiveData<AllContactsResponse> )
    {
        list?.clear()
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
        pDialog.show()
        liveData.observe(this , Observer { data ->
            val res = data?.response


            if (pDialog.isShowing)
            {
                pDialog.dismissWithAnimation()
            }

            if (res != "404") {
                if (data != null) {
                    list?.add(data)
                    adapter?.submitList(list)
                    adapter?.notifyDataSetChanged()
                }
            }

        })
    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
