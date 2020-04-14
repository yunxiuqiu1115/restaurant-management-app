package com.example.final_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.final_project.R
import com.example.final_project.adaptor.OrderAdapter
import com.example.final_project.adaptor.PopularAdapter
import com.example.final_project.util.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_appetizer.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    var mainList : ArrayList<Food> = ArrayList()
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        Log.d("mylog","This is oncreate page")
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("mylog","This is oncreateview page")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrderAdapter(mainList)
        main_recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        main_recyclerView.adapter = adapter
        Log.d("mylog","This is viewCreated page")
        db.collection("food")
            .whereEqualTo("sort","main")
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        Log.d("reach","Don't have a Menu")
                    } else{
                        mainList.clear()
                        for(document in task.result!!){
                            mainList.add(
                                Food(
                                    document.get("id").toString(),
                                    document.get("name").toString(),
                                    document.get("amount").toString().toInt(),
                                    document.get("description").toString(),
                                    document.get("available").toString().toBoolean(),
                                    document.get("image").toString(),
                                    document.get("discount").toString().toInt(),
                                    document.get("ordertimes").toString().toInt(),
                                    document.get("price").toString().toDouble(),
                                    document.get("sort").toString()
                                ))
                        }
                        Log.d("mylog",mainList.toString())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed")
                }
            }
    }
}




