package com.example.final_project.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.final_project.R
import com.example.final_project.activities.HistoryActivity
import com.example.final_project.activities.HomeActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : Fragment() {
    val mAuth = FirebaseAuth.getInstance()
    lateinit var db : FirebaseFirestore
    private val uid =  mAuth.uid
    private var profileid : String?=null
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
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onStart(){
        super.onStart()
        updateUI()
        update_profile.setOnClickListener{
            dialogView()
        }
        edit_profile_text.setOnClickListener{
            dialogView()
        }

        order_history.setOnClickListener{
            val intent = Intent(this.context, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUser(){
        val uid = mAuth.uid?:""
        val address = "Address: "
        profile_address.text = address
        val name = "Your name: "
        profile_name.text = name
        val birthday = "01/01/1990"
        profile_birthday.text = birthday
        val email = mAuth.currentUser!!.email
        profile_email.text = email

        val username = "username"
        profile_username.text = "@$username"
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
            .addOnFailureListener {

            }

    }
    private fun updateUI(){
        db.collection("users")
            .whereEqualTo("uid",uid)
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        createUser()
                    } else{
                        for(document in task.result!!){
                            profileid = document.id
                            profile_name.text = document.get("name").toString()
                            profile_username.text = "@"+document.get("username").toString()
                            profile_phone.text = document.get("phone").toString()
                            profile_email.text = document.get("email").toString()
                            profile_address.text = document.get("address").toString()
                            profile_birthday.text = document.get("birthday").toString()
                        }
                    }
                }
            }
    }
    private fun dialogView(){
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_edit,null)
        val mBuilder = AlertDialog.Builder(this.context)
            .setView(dialogView)
            .setTitle("Please Update Your Profile")

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawableResource(R.drawable.rounded_button)
        retrieve(mAlertDialog)
        mAlertDialog.edit_button.setOnClickListener{
            submitUpdate(mAlertDialog)
            updateUI()
            mAlertDialog.dismiss()
        }

    }
    private fun retrieve(mAlertDialog : AlertDialog){
        db.collection("users")
            .whereEqualTo("uid",uid)
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        createUser()
                    } else{
                        for(document in task.result!!){
                            profileid = document.id
                            mAlertDialog.edit_name.setText(document.get("name").toString())
                            mAlertDialog.edit_username.setText(document.get("username").toString())
                            mAlertDialog.edit_phone.setText(document.get("phone").toString())
                            mAlertDialog.edit_email.setText(document.get("email").toString())
                            mAlertDialog.edit_address.setText(document.get("address").toString())
                            mAlertDialog.edit_birthday.setText(document.get("birthday").toString())
                        }
                    }
                }
            }
    }
    private fun submitUpdate(mAlertDialog : AlertDialog){
        val userMap : MutableMap<String,Any> = HashMap()
        userMap["uid"] = uid!!
        userMap["address"] = mAlertDialog.edit_address.text.toString()
        userMap["name"] = mAlertDialog.edit_name.text.toString()
        userMap["birthday"] = mAlertDialog.edit_birthday.text.toString()
        userMap["email"] = mAlertDialog.edit_email.text.toString()
        userMap["username"] = mAlertDialog.edit_username.text.toString()
        userMap["phone"] = mAlertDialog.edit_phone.text.toString()

        db.collection("users")
            .document(profileid!!)
            .update(userMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

    }
}
