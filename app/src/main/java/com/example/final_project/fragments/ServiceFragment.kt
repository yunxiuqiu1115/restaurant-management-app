package com.example.final_project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.final_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class ServiceFragment : Fragment() {
    val mAuth = FirebaseAuth.getInstance()
    lateinit var db : FirebaseFirestore
    private val uid =  mAuth.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onStart(){
        super.onStart()
        db.collection("users")
            .whereEqualTo("uid",uid)
            .get()
            .addOnCompleteListener({task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        createUser()

                    }
                    else{
                        for(document in task.result!!){

                        }
                    }
                }
            })
    }

    private fun createUser(){
        val uid = mAuth.uid?:""
        val address = "Address: "
        val name = "Username: "
        val birthday = "01/01/1990"
        val email = mAuth.currentUser!!.email
        val username = mAuth.currentUser!!.displayName
        val phone = "+1 585 077 2221"


    }
}
