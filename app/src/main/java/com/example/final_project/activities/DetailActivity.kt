package com.example.final_project.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.final_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.order_food.*
import kotlinx.android.synthetic.main.order_food.view.*

class DetailActivity : AppCompatActivity() {
    lateinit var id:String
    lateinit var name:String
    lateinit var description:String
    private var discount = 0
    lateinit var db : FirebaseFirestore
    private var ordertimes = 0
    private var available = true
    private var amount = 0
    lateinit var image:String
    private var price = 0.0
    lateinit var sort:String
    var amountArray:ArrayList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        get()
    }

    override fun onStart(){
        super.onStart()
        get()
        popular_food_name.text = name
        Picasso.get().load(image).resize(400, 400) // resize the image to these dimensions (in pixel)
            .centerCrop().into(popular_food_image)
        popular_food_description.text = description
        if(discount==0){
            popular_food_price.text = price.toString()
        }
        else{
            val num = Math.round((price - (discount/100.0)*price) * 100.0) / 100.0
            popular_food_price.text = "Original: $$price\n\n Now: $$num!"
        }
        Log.d("amount", "test $amount")
        if(available){
            orderFood.visibility = View.VISIBLE
        }
        else{
            popular_food_image.alpha= 0.5f
            soldOut.text = "SOLD OUT!!!"
        }

        back_button.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        orderFood.setOnClickListener{
            dialogView()
        }

    }

    private fun get(){
        id = intent!!.getStringExtra("id")
        name = intent!!.getStringExtra("name")
        description = intent!!.getStringExtra("description")
        discount = intent!!.getIntExtra("discount",discount)
        available = intent!!.getBooleanExtra("available",available)
        amount = intent!!.getIntExtra("amount",amount)
        image = intent!!.getStringExtra("image")
        price = intent!!.getDoubleExtra("price",price)
        ordertimes = intent!!.getIntExtra("ordertimes",ordertimes)
        sort = intent!!.getStringExtra("sort")
    }
    private fun dialogView(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.order_food,null)
        var adapter = ArrayAdapter<Int>(this,android.R.layout.simple_spinner_item,amountArray)
        val mBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Please choose the amount you wanna order")
        val mAlertDialog = mBuilder.show()
        dialogView.amountSpinner.adapter = adapter
        for(i in 1..amount){
            amountArray.add(i)
        }
        Log.d("debug ","size "+amountArray.size)
        adapter.notifyDataSetChanged()
        var number = dialogView.amountSpinner.selectedItemPosition + 1
        mAlertDialog.amountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long){
                mAlertDialog.total_price.text = "Total: "+(pos+1) * price
                number = pos + 1
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                mAlertDialog.total_price.text = "Total: "+ 0
            }

        }

        mAlertDialog.order_food_button.setOnClickListener{
            updateFood(number)
            mAlertDialog.dismiss()
            put()

        }

    }
    private fun updateFood(number: Int){
        val foodMap : MutableMap<String,Any> = HashMap()
//        Log.d("Id",id)
        foodMap["name"] = name
        ordertimes += 1
        foodMap["ordertimes"] = ordertimes
        foodMap["description"] = description
        foodMap["discount"] = discount
        amount -= number
        foodMap["amount"] = amount
        if(amount==0){
            available = false
            foodMap["available"] = available
        }
        else{
            foodMap["available"] = true
        }
        foodMap["image"] = image
        foodMap["price"] = price
        foodMap["sort"] = sort
        db.collection("food")
            .document(id!!)
            .update(foodMap)
            .addOnSuccessListener {
                Log.d("Success","Task Completed!")
            }
            .addOnFailureListener {
                Log.d("Fail","Task Failed!")
            }
    }
    private fun put(){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("name",name)
        intent.putExtra("description",description)
        intent.putExtra("discount",discount)
        intent.putExtra("available",available)
        intent.putExtra("amount",amount)
        intent.putExtra("image",image)
        intent.putExtra("price",price)
        intent.putExtra("ordertimes",ordertimes)
        intent.putExtra("sort",sort)
        startActivity(intent)
    }

}
