package com.example.final_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project.R
import com.example.final_project.adaptor.HistoryAdapter
import com.example.final_project.fragments.ServiceFragment
import com.example.final_project.util.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    var historyList : ArrayList<Order> = ArrayList()
    lateinit var db: FirebaseFirestore
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.uid!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
    }

    override fun onStart(){
        super.onStart()
        val adapter = HistoryAdapter(historyList)
        history_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        history_recyclerView.adapter = adapter
        update(adapter)
        backButton.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
    private fun update(adapter : HistoryAdapter){
        db.collection("orders")
            .whereEqualTo("uid",uid)
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        Log.d("reach","Don't have history")
                    } else{
                        Log.d("reach","Have history")
                        historyList.clear()
                        for(document in task.result!!){
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
                        Log.d("reach","size "+historyList.size)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed")
                }
            }
    }

}
