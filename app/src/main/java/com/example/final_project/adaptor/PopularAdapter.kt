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

class PopularViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.food_item,parent,false)){
    private val foodImage: ImageView
    private val foodName: TextView

    init{
        foodImage = itemView.findViewById(R.id.food_image)
        foodName = itemView.findViewById(R.id.food_name)
    }

    fun bind(popularFood: Food){
        foodName?.text = popularFood.name
        Picasso.get().load(popularFood.image).resize(400, 400) // resizes the image to these dimensions (in pixel)
            .centerCrop().into(foodImage)

    }

}

class PopularAdapter(private val list:ArrayList<Food>)
    : RecyclerView.Adapter<PopularViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):PopularViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return PopularViewHolder(inflater,parent)
    }
    override fun onBindViewHolder(holder:PopularViewHolder,position:Int){
        val popularFood:Food = list[position]
        holder.bind(popularFood)
    }
    override fun getItemCount():Int = list.size
}