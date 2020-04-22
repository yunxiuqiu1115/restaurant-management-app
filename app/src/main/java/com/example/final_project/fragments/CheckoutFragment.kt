package com.example.final_project.fragments



import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.final_project.R
import com.example.final_project.adaptor.CheckoutAdapter
import com.example.final_project.util.Order
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.fragment_order_checkout.*
import kotlinx.android.synthetic.main.payment_dialog.*

class CheckoutFragment : Fragment() {

    var checkoutList: ArrayList<Order> = ArrayList()
    var idList:ArrayList<String> = ArrayList()
    lateinit var db: FirebaseFirestore
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.uid!!
    private var totalPrice = 0f
    private var count = 0
    private lateinit var adapter: CheckoutAdapter






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
        adapter = CheckoutAdapter(checkoutList, idList)
        checkout_recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        checkout_recyclerView.adapter = adapter
        update(adapter)

        checkout_button.setOnClickListener {
            dialogView()
        }



    }

    private fun dialogView(){
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.payment_dialog,null)
        val mBuilder = AlertDialog.Builder(this.context)
            .setView(dialogView)
            .setTitle("Please Choose your payment method")

        val mAlertDialog = mBuilder.show()


        mAlertDialog.google_pay.setOnClickListener{

            paymentSuccessUpdate()
            mAlertDialog.dismiss()
        }

        mAlertDialog.paypal.setOnClickListener{

            paymentSuccessUpdate()
            mAlertDialog.dismiss()
        }
        mAlertDialog.wechat.setOnClickListener{

            paymentSuccessUpdate()
            mAlertDialog.dismiss()
        }
        mAlertDialog.credit_card.setOnClickListener{

            paymentSuccessUpdate()
            mAlertDialog.dismiss()
        }


        mAlertDialog.payment_cancel_button.setOnClickListener{


            mAlertDialog.dismiss()
        }

    }

    private fun paymentSuccessUpdate()
    {
        val resId = 0
        val takeout = false
        //create the history that has all the items and can be checked
        val dbData = hashMapOf("items" to checkoutList,
                                "resId" to resId,
                                "takeout" to takeout)
        db.collection("history")

            .add(dbData)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> {
                Log.d("mylog", "successful")

            })
            .addOnFailureListener { e ->
                Log.w("mylog", "Error deleting document", e)

            }

        //need to decrement the number of food


        //clear the current checkout_list
        for (id in idList) {
            db.collection("orders").document("$id")
                .delete()
                .addOnSuccessListener {
                    Log.d(
                        "mylog",
                        "DocumentSnapshot successfully deleted!"
                    )
                }
                .addOnFailureListener { e -> Log.w("mylog", "Error deleting document", e) }
        }
        idList.clear()
        checkoutList.clear()

        update(adapter)

        val myToast = Toast.makeText(this.context, "Successful! You can view it in Profile->order history.", Toast.LENGTH_SHORT)

        myToast.show()





    }

    private fun update(adapter: CheckoutAdapter) {
        totalPrice = 0f

        db.collection("orders")
            .whereEqualTo("uid", uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result!!.isEmpty) {
                        Log.d("reach", "Don't have history")
                        grand_total.text = "Total: $0.00"
                        checkout_button.visibility = View.INVISIBLE
                    } else {
                        Log.d("reach", "Have history")

                        checkoutList.clear()
                        idList.clear()
                        for (document in task.result!!) {
                            checkoutList.add(
                                Order(
                                    uid,
                                    document.get("foodname").toString(),
                                    document.get("time").toString(),
                                    document.get("amount").toString().toInt(),
                                    document.get("totalprice").toString().toFloat()
                                )
                            )
                            idList.add(document.id)
                            Log.d("mylog", idList.toString())
                            totalPrice+= document.get("totalprice").toString().toFloat()
                            count +=1

                        }
                        checkoutList.sortByDescending { it.time }
                        Log.d("reach", "size " + checkoutList.size)
                        grand_total.text = "Total: $ $totalPrice"
                        checkout_button.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed")
                }
            }
    }
}
