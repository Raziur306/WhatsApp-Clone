package com.eritlab.whatsappcone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eritlab.whatsappcone.databinding.RecyclerContactItemBinding
import com.eritlab.whatsappcone.model.ContactModel

class ContactRecyclerAdapter(
    private val contactList: ArrayList<ContactModel>,
) : RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder>() {
    class ViewHolder(val binding: RecyclerContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerContactItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.contactNumber.text = contactList[position].phoneNumber
        holder.binding.contactName.text = contactList[position].name

    }

    override fun getItemCount(): Int {
        return contactList.size
    }


}