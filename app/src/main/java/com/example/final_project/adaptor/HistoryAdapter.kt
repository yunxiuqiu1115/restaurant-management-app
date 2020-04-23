package com.example.final_project.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.R
import com.example.final_project.activities.DetailActivity
import com.example.final_project.util.History

import com.squareup.picasso.Picasso


class HistoryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.history_item,parent,false)){
    private val totalPrice: TextView = itemView.findViewById(R.id.history_total_price)
    private val type : TextView = itemView.findViewById(R.id.history_type)
    private val branchLocation : TextView = itemView.findViewById(R.id.history_location)
    private val time : TextView = itemView.findViewById(R.id.history_time)

    fun bind(history: History){
        totalPrice.text = "Total Price: $"+history.totalPrice.toString()
        type.text = history.type
        branchLocation.text = "Order placed at:"+history.location
        time.text = "Order Time:"+history.time

    }

}

class HistoryAdapter(private val list:ArrayList<History>)
    : RecyclerView.Adapter<HistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):HistoryViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return HistoryViewHolder(inflater,parent)
    }
    override fun onBindViewHolder(holder:HistoryViewHolder,position:Int){
        val history: History = list[position]
        holder.bind(history)
    }
    override fun getItemCount():Int = list.size
}