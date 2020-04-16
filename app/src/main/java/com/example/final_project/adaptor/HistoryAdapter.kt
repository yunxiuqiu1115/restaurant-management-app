package com.example.final_project.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.activities.DetailActivity
import com.example.final_project.util.Order
import com.squareup.picasso.Picasso


class HistoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.history_item,parent,false)){
    private val name: TextView = itemView.findViewById(R.id.history_food_name)
    private val time : TextView = itemView.findViewById(R.id.history_time)
    private val amount : TextView = itemView.findViewById(R.id.history_amount)
    private val total : TextView = itemView.findViewById(R.id.history_total)

    fun bind(order: Order){
        name.text = order.foodname
        time.text = order.time
        amount.text = order.amount.toString()
        total.text = order.total_price.toString()
    }

}

class HistoryAdapter(private val list:ArrayList<Order>)
    : RecyclerView.Adapter<HistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):HistoryViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return HistoryViewHolder(inflater,parent)
    }
    override fun onBindViewHolder(holder:HistoryViewHolder,position:Int){
        val order:Order = list[position]
        holder.bind(order)
    }
    override fun getItemCount():Int = list.size
}