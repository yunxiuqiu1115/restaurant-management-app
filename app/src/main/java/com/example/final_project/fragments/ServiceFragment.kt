package com.example.final_project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.final_project.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_service.*

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
                            profile_name.text = document.get("name").toString()
                            profile_username.text = "@"+document.get("username").toString()
                            profile_phone.text = document.get("phone").toString()
                            profile_email.text = document.get("email").toString()
                            profile_address.text = document.get("address").toString()
                            profile_birthday.text = document.get("birthday").toString()
                        }
                    }
                }
            })
    }

    private fun createUser(){
        val uid = mAuth.uid?:""
        val address = "Address: "
        profile_address.text = address
        val name = "Username: "
        profile_name.text = name
        val birthday = "01/01/1990"
        profile_birthday.text = birthday
        val email = mAuth.currentUser!!.email
        profile_email.text = email
        val username = "username"
        profile_username.text = "@"+username
        val phone = "+1 585 077 2221"
        profile_phone.text = phone

        val userMap : MutableMap<String,Any> = HashMap()
        userMap["uid"] = uid!!
        userMap["address"] = address!!
        userMap["name"] = name!!
        userMap["birthday"] = birthday!!
        userMap["email"] = email!!
        userMap["username"] = username!!
        userMap["phone"] = phone!!

        db.collection("users")
            .add(userMap)
            .addOnSuccessListener(OnSuccessListener<DocumentReference>{

            })
            .addOnFailureListener({

            })

    }
}
