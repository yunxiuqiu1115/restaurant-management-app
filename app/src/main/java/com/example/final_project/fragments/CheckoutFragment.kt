package com.example.final_project.fragments



import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project.R
import com.example.final_project.adaptor.CheckoutAdapter
import com.example.final_project.util.Order
import com.example.final_project.util.Restaurant
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_order_checkout.*


import kotlinx.android.synthetic.main.payment_dialog.*
import kotlinx.android.synthetic.main.payment_dialog.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CheckoutFragment : Fragment() {

    var checkoutList: ArrayList<Order> = ArrayList()
    var idList:ArrayList<String> = ArrayList()
    lateinit var db: FirebaseFirestore
    val mAuth = FirebaseAuth.getInstance()
    val uid = mAuth.uid!!
    private var totalPrice = 0f
    private var count = 0
    private lateinit var adapter: CheckoutAdapter
    private var resList:ArrayList<Restaurant> = ArrayList()
    var resArray:ArrayList<String> = ArrayList()

    private var resId = 0
    private var type = ""
    private var resName = ""
    private val optionArray = arrayOf<String>("Dine in","Take out")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        getResList()
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

        adapter = CheckoutAdapter(checkoutList, idList,grand_total)
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





        var closest = 0


        var ooptionAdapter = ArrayAdapter<String>(this.activity,R.layout.support_simple_spinner_dropdown_item,optionArray)
        dialogView.option_spinner.adapter = ooptionAdapter

        var resAdapter = ArrayAdapter<String>(this.activity,R.layout.support_simple_spinner_dropdown_item,resArray)
        dialogView.res_spinner.adapter = resAdapter

        adapter.notifyDataSetChanged()
        resName = resArray[resId]
        mAlertDialog.option_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long){
                if(pos == 0){
                    type = "Dine in"
                }
                else{
                    type = "Take out"
                }
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                type = "Take out"
            }
        }




        mAlertDialog.res_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long){
                resId = pos
                resName = resArray[resId]
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                resId = 0
            }
        }



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
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)


        //create the history that has all the items and can be checked
        val dbData = hashMapOf("items" to checkoutList,
                                "resId" to resId,
                                "type" to type,
                                "time" to formatted,
                                "branch_name" to resName,
                                "total_price" to totalPrice,
                                "uid" to uid

                                )
        db.collection("history")

            .add(dbData)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> {
                Log.d("mylog", "successful")

            })
            .addOnFailureListener { e ->
                Log.w("mylog", "Error deleting document", e)

            }

        //need to decrement the number of food
//        var foodMap : MutableMap<String,Any> = HashMap()
//        var docList:ArrayList<foodAmount> = ArrayList()
//        for (item in checkoutList){
//            db.collection("food")
//                .whereEqualTo("name", item.foodname)
//                .get()
//                .addOnCompleteListener { task->
//                    if(task.isSuccessful){
//                        for (document in task.result!!){
//                            docList.add(foodAmount(document.id,item.amount))
//
//                        }
//                    }
//                }
//        }
//        foodMap["amount"]
//        for (item in docList){
//            db.collection("food")
//                .document(item.docId)
//                .update({amount: 100})
//                .addOnSuccessListener {
//                    Log.d("Success","Task Completed!")
//                }


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

    private fun getResList(){
        db.collection("Restaurant")
            .get()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    if(task.result!!.isEmpty){
                        Log.d("reach","Don't have history")
                    } else{

                        resList.clear()
                        resArray.clear()
                        for(document in task.result!!){
                            resList.add(
                                Restaurant(

                                    document.id,
                                    document.get("location_description").toString(),
                                    document.get("lat").toString().toDouble(),
                                    document.get("long").toString().toDouble()
                                )
                            )
                        }


                        for (item in resList){
                            resArray.add(item.term)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    println("failed")
                }
            }
        .addOnFailureListener {

        }
    }

}
