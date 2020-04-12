package com.example.final_project.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.util.Food
import com.squareup.picasso.Picasso

class DealViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.deal_item,parent,false)){
    private val foodImage: ImageView
    private val foodDiscount: TextView

    init{
        foodImage = itemView.findViewById(R.id.food_image2)
//        foodName = itemView.findViewById(R.id.food_name)
        foodDiscount = itemView.findViewById(R.id.food_discount2)
    }

    fun bind(popularFood: Food){
        foodDiscount?.text = popularFood.discount.toString() + "% OFF!"
        Picasso.get().load(popularFood.image).resize(500, 500) // resizes the image to these dimensions (in pixel)
            .centerCrop().into(foodImage)

    }

}

class DealAdapter(private val list:ArrayList<Food>)
    : RecyclerView.Adapter<DealViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):DealViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return DealViewHolder(inflater,parent)
    }
    override fun onBindViewHolder(holder:DealViewHolder,position:Int){
        val popularFood:Food = list[position]
        holder.bind(popularFood)
    }
    override fun getItemCount():Int = list.size
}