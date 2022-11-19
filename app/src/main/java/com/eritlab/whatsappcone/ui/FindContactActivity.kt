package com.eritlab.whatsappcone.ui

import android.Manifest.permission.READ_CONTACTS
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.eritlab.whatsappcone.R
import com.eritlab.whatsappcone.adapter.ContactRecyclerAdapter
import com.eritlab.whatsappcone.databinding.ActivityFindContactBinding
import com.eritlab.whatsappcone.model.ContactModel

class FindContactActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
    private var contactListPermissionGranted = false
    private val contactList = ArrayList<ContactModel>()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var binding: ActivityFindContactBinding
    private var phonesHashmap: HashMap<Long, ArrayList<String>> = HashMap()
    private val PROJECTION_NUMBERS = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    private val PROJECTION_DETAILS = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindContactBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.default_color)
        setContentView(binding.root)


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
                contactListPermissionGranted =
                    permission ?: contactListPermissionGranted
            }

        //check and launch for permission
        updateOrRequestPermission()

        //searchbar

//        val searchView =
//            binding.searchToolbar.searchToolbarMenu.findViewById<ActionMenuItemView>(R.id.action_filter_search)


        // searchView.onActionViewExpanded()
        //appbar
        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                //R.id.action_search -> {
                // binding.searchToolbar.root.visibility = View.VISIBLE
//                    ObjectAnimator.ofFloat(binding.searchToolbar, "translationX", 100f).apply {
//
//                        duration = 2000
//                        start()
//                    }
                //    true
                //  }
                R.id.inviteFriend -> {
                    true
                }
                R.id.contacts -> {
                    true
                }
                R.id.refresh -> {
                    binding.topAppBar.menu.findItem(R.id.contactProgressBar).isVisible = true
                    LoaderManager.getInstance(this).initLoader(0, null, this)
                    true
                }
                R.id.help -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

    }


    private fun updateOrRequestPermission() {
        val readPermission = ContextCompat.checkSelfPermission(
            this, READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        contactListPermissionGranted = readPermission
        val permissionList = mutableListOf<String>()
        if (!contactListPermissionGranted)
            permissionLauncher.launch(READ_CONTACTS)

        if (contactListPermissionGranted) {
            readContact()
        }
    }

    private fun readContact() {
        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when (id) {
            0 -> {
                return CursorLoader(
                    this,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION_NUMBERS,
                    null,
                    null,
                    null
                )

            }
            else -> {
                return CursorLoader(
                    this,
                    ContactsContract.Contacts.CONTENT_URI,
                    PROJECTION_DETAILS,
                    null,
                    null,
                    null
                )
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        when (loader.id) {
            0 -> {
                if (data != null) {
                    while (!data.isClosed && data.moveToNext()) {
                        val contactId = data.getLong(0)
                        val phone = data.getString(1)
                        val list: ArrayList<String>
                        if (phonesHashmap.containsKey(contactId)) {
                            list = phonesHashmap[contactId]!!
                        } else {
                            list = ArrayList()
                            phonesHashmap[contactId] = list
                        }
                        list.add(phone)
                    }
                }
                data?.close()
                LoaderManager.getInstance(this).initLoader(1, null, this)
            }
            1 -> {
                if (data != null) {
                    while (!data.isClosed && data.moveToNext()) {
                        val id = data.getLong(0)
                        val name = data.getString(1)
                        val contactPhone = phonesHashmap[id]
                        Log.d("Contact HashMap", phonesHashmap.toString())
                        if (contactPhone?.size != 0 && contactPhone != null) {
                            var phoneNumber = contactPhone[0].replace("-", "").replace(
                                " ", ""
                            )
                            if ((phoneNumber.subSequence(0, 1)) != "+") {
                                phoneNumber = "+88$phoneNumber"
                            }
                            contactList.add(
                                ContactModel(
                                    name,
                                    phoneNumber
                                )
                            )

                        }
                    }
                }
                data?.close()
            }
        }
        binding.topAppBar.subtitle = contactList.size.toString() + " contacts"

        if (contactList.size != 0) {
            binding.contactRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.contactRecyclerView.adapter = ContactRecyclerAdapter(contactList)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }
}