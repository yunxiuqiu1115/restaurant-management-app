package com.example.final_project.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.activities.DetailActivity
import com.example.final_project.util.Food
import com.squareup.picasso.Picasso


class PopularViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.food_item,parent,false)){
    private val foodImage: ImageView = itemView.findViewById(R.id.food_image)

    //    private lateinit var activity: AppCompatActivity
    private val foodName: TextView = itemView.findViewById(R.id.food_name)

    init{
        //        activity = HomeActivity()
    }

    fun bind(food: Food){
        foodName?.text = food.name
        Picasso.get().load(food.image).resize(600, 600) // resizes the image to these dimensions (in pixel)
            .centerCrop().into(foodImage)
        foodImage.clipToOutline = true

        foodImage.setOnClickListener{
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("id",food.id)
            intent.putExtra("name",food.name)
            intent.putExtra("description",food.description)
            intent.putExtra("discount",food.discount)
            intent.putExtra("available",food.available)
            intent.putExtra("amount",food.amount)
            intent.putExtra("image",food.image)
            intent.putExtra("price",food.price)
            intent.putExtra("ordertimes",food.ordertimes)
            intent.putExtra("sort",food.sort)
            it.context.startActivity(intent)
        }

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