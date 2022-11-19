package com.eritlab.whatsappcone.ui

import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
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
        setContentView(binding.root)


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
                contactListPermissionGranted =
                    permission ?: contactListPermissionGranted
            }

        //check and launch for permission
        updateOrRequestPermission()
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

        if (contactList.size != 0) {
            binding.contactRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.contactRecyclerView.adapter = ContactRecyclerAdapter(contactList)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }
}