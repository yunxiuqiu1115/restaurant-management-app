package com.example.final_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_project.R
import com.example.final_project.adaptor.PopularAdapter
import com.example.final_project.util.Food
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var popularfoodList: ArrayList<Food> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        popularfoodList.add(Food(
            "1",
            "Hamburger",
            "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2004/2/25/0/bw2b07_hambugers1.jpg.rend.hgtvcom.826.620.suffix/1558017418187.jpeg"
        ))
        popularfoodList.add(Food(
            "2",
            "Steak",
            "https://i2.wp.com/www.foodrepublic.com/wp-content/uploads/2012/05/testkitchen_argentinesteak.jpg"
        ))
        popularfoodList.add(Food(
            "3",
            "Budae Jigae",
            "https://mykoreankitchen.com/wp-content/uploads/2016/02/7.-Korean-Army-Stew-Budae-Jjigae.jpg"
        ))
        popularfoodList.add(Food(
            "4",
            "Ramen",
            "https://www.simplyrecipes.com/wp-content/uploads/2019/09/IP-Chicken-Wing-Ramen-LEAD-4.jpg"
        ))
        val adapter = PopularAdapter(popularfoodList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val intent = Intent(this,InfoActivity::class.java)
        startActivity(intent)
    }
}
