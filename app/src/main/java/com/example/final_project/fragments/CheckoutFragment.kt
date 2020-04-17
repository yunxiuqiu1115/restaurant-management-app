package com.example.final_project.fragments



import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.final_project.R
import com.example.final_project.activities.HomeActivity
import com.example.final_project.adaptor.HistoryAdapter
import com.example.final_project.adaptor.OrderAdapter
import com.example.final_project.adaptor.PopularAdapter
import com.example.final_project.util.Food
import com.example.final_project.util.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_appetizer.*
import kotlinx.android.synthetic.main.fragment_dessert.*
import kotlinx.android.synthetic.main.fragment_home.*

class CheckoutFragment : Fragment() {

    var historyList: ArrayList<Order> = ArrayList()
    lateinit var db: FirebaseFirestore
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.uid!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_checkout, container, false)
    }

    override fun onStart() {
        super.onStart()
        val adapter = HistoryAdapter(historyList)
        history_recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        history_recyclerView.adapter = adapter
        update(adapter)
        backButton.setOnClickListener {
            val intent = Intent(this.context, HomeActivity::class.java)
            startActivity(intent)
        }


    }

    private fun update(adapter: HistoryAdapter) {
        db.collection("orders")
            .whereEqualTo("uid", uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.isEmpty) {
                        Log.d("reach", "Don't have history")
                    } else {
                        Log.d("reach", "Have history")
                        historyList.clear()
                        for (document in task.result!!) {
                            historyList.add(
                                Order(
                                    uid,
                                    document.get("foodname").toString(),
                                    document.get("time").toString(),
                                    document.get("amount").toString().toInt(),
                                    document.get("totalprice").toString().toDouble()
                                )
                            )
                        }
                        historyList.sortByDescending { it.time }
                        Log.d("reach", "size " + historyList.size)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed")
                }
            }
    }
}
