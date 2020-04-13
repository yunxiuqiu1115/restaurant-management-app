package com.example.final_project.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.final_project.R
import com.example.final_project.adaptor.DealAdapter
import com.example.final_project.adaptor.PopularAdapter
import com.example.final_project.util.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    var foodList : ArrayList<Food> = ArrayList()
    var popularList : ArrayList<Food> = ArrayList()
    var dealList : ArrayList<Food> = ArrayList()
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set up database
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.setFirestoreSettings(settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart(){
        super.onStart()
        val adapter = PopularAdapter(popularList)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = adapter

        val adapter2 = DealAdapter(dealList)
        recyclerView2.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
        recyclerView2.adapter = adapter2


        db.collection("food")
            .get()
            .addOnCompleteListener({
                if(it.isSuccessful){
                    foodList.clear()
                    for(document in it.result!!){
                        foodList.add(
                            Food(
                                document.id.toString(),
                                document.get("name").toString(),
                                document.get("amount").toString().toInt(),
                                document.get("description").toString(),
                                document.get("available").toString().toBoolean(),
                                document.get("image").toString(),
                                document.get("discount").toString().toInt(),
                                document.get("ordertimes").toString().toInt(),
                                document.get("price").toString().toDouble(),
                                document.get("sort").toString()
                            )
                        )
                    }
                    foodList.sortByDescending{it.ordertimes}
                    popularList.clear()
                    for(i in 0..4){
                        popularList.add(foodList[i])
//                        dealList.add(foodList[i])
                    }
                    adapter.notifyDataSetChanged()

                    dealList.clear()
                    foodList.sortByDescending{it.discount}
                    for(i in 0..4){
                        if(foodList[i].discount==0)break;
                        dealList.add(foodList[i])
                    }
                    adapter2.notifyDataSetChanged()



                } else {
                    println("failed")
                    Log.d("debug","failed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                }
            })

    }

}