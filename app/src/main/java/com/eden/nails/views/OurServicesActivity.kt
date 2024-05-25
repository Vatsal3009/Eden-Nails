package com.eden.nails.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.eden.nails.R
import com.eden.nails.adapter.OurServicesTitleAdapter
import com.eden.nails.base.BaseActivity
import com.eden.nails.databinding.ActivityOurServicesBinding
import com.eden.nails.databinding.BottomSheetSelectDateBinding
import com.eden.nails.model.Service
import com.eden.nails.model.ServiceListModel
import com.eden.nails.utils.constructWhatsappMessage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OurServicesActivity : BaseActivity<ActivityOurServicesBinding>() {

    private lateinit var database: FirebaseDatabase
    private lateinit var servicesRef: DatabaseReference
    private lateinit var ourServicesTitleAdapter: OurServicesTitleAdapter
    private var selectedDate: String = ""
    val serviceTitleList = mutableListOf<ServiceListModel>()


    override fun getViewBinding(): ActivityOurServicesBinding =
        ActivityOurServicesBinding.inflate(layoutInflater)

    override fun initControl() {

        binding.loader.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance()
        servicesRef = database.getReference("services")

        // Retrieve data from Firebase
        servicesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (serviceSnapshot in dataSnapshot.children) {
                    val serviceTitle =
                        serviceSnapshot.child("service_title").getValue(String::class.java)

                    val serviceList =
                        mapServiceSnapshotToList(serviceSnapshot.child("service_list"))
                            ?: continue  // Skip if service_list is null

                    serviceTitleList.add(
                        ServiceListModel(
                            serviceTitle = serviceTitle,
                            serviceList = serviceList
                        )
                    )
                    Log.e("==>vatsal", serviceList.toString())

                }


                binding.loader.visibility = View.GONE
                ourServicesTitleAdapter =
                    OurServicesTitleAdapter(serviceTitleList as ArrayList<ServiceListModel>) {clickedItem->
                        serviceTitleList.forEach { serviceModel ->
                            if (serviceModel != clickedItem) {
                                serviceModel.isExpanded = false
                                // Notify adapter of the change in each item's expansion state
                                ourServicesTitleAdapter.notifyItemChanged(serviceTitleList.indexOf(serviceModel))
                            }
                        }

                        // Toggle the clicked item's expansion state
                        clickedItem.isExpanded = !clickedItem.isExpanded

                        // Notify adapter of the change in the clicked item's expansion state
                        ourServicesTitleAdapter.notifyItemChanged(serviceTitleList.indexOf(clickedItem))

                    }
                binding.rvOurServices.adapter = ourServicesTitleAdapter

                // Now you have the list of services, you can use it as needed
                // For example, you can display it in a RecyclerView, ListView, or any other UI component
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        binding.btnDate.setOnClickListener {
            val dialogueBinding = BottomSheetSelectDateBinding.inflate(layoutInflater)
            val dialogue = BottomSheetDialog(this)
            dialogueBinding.apply {
                icClose.setOnClickListener {
                    dialogue.dismiss()
                }
                btnCancel.setOnClickListener {
                    dialogue.dismiss()
                }
                btnDone.setOnClickListener {
                    if (cbConfirmation.isChecked == false) {
                        Toast.makeText(
                            this@OurServicesActivity,
                            "Please confirm to proceed",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val selectedYear = datePicker.year
                        val selectedMonth = datePicker.month
                        val selectedDayOfMonth = datePicker.dayOfMonth
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                        val currentCalendar = Calendar.getInstance()
                        if (selectedCalendar <= currentCalendar) {
                            Toast.makeText(
                                this@OurServicesActivity,
                                "Please select a future date",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
                        selectedDate = dateFormat.format(selectedCalendar.time)
                        binding.btnDate.text = selectedDate
                        dialogue.dismiss()
                    }
                }
            }
            dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogue.window?.setBackgroundDrawableResource(R.color.transparent)
            dialogue.behavior.isDraggable = false
            dialogue.setCanceledOnTouchOutside(false)
            dialogue.setContentView(dialogueBinding.root)
            dialogue.show()
        }

        binding.btnBookAppointment.setOnClickListener {
            val serviceList = mutableListOf<Service>()
            serviceTitleList.forEach { serviceModel ->
                serviceModel.serviceList?.filter { it?.isServiceSelected == true }
                    ?.forEach { selectedService ->
                        selectedService?.let {
                            serviceList.add(it)
                        }
                    }
            }

            if (serviceList.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show()
            } else if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            } else {
                openWhatsAppChat(
                    constructWhatsappMessage(
                        serviceList,
                        selectedDate
                    ), "+917567155240"
                )
            }
        }

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun mapServiceSnapshotToList(serviceListSnapshot: DataSnapshot): List<Service>? {
        val serviceItems = mutableListOf<Service>()
        for (serviceItemSnapshot in serviceListSnapshot.children) {

            val service = serviceItemSnapshot.getValue(Service::class.java)
            Log.e("==vatsal", service.toString())

            if (service != null) {
                serviceItems.add(service)
            }
        }
        return if (serviceItems.isEmpty()) null else serviceItems
    }


    private fun openWhatsAppChat(message: String, phoneNumber: String) {
        // Create a Uri with the WhatsApp URL including the phone number
        val uri = Uri.parse("https://wa.me/$phoneNumber/?text=${Uri.encode(message)}")

        // Create an Intent with ACTION_VIEW and the Uri
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Set the package to com.whatsapp to ensure it opens in WhatsApp if it's installed
        intent.setPackage("com.whatsapp")

        // Check if there's an activity to handle this intent before starting it
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // If WhatsApp is not installed, you can show a message or take alternative action
            // For example, open WhatsApp in the browser
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }


    companion object {
        fun callOurServicesActivity(context: Context): Intent {
            return Intent(context, OurServicesActivity::class.java)
        }
    }

}