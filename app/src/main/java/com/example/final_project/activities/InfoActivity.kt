package com.example.final_project.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.final_project.R


class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_info_page)


    }

    override fun onStart() {
        super.onStart()
        val mapClick = findViewById<ImageView>(R.id.screenshot_map)
        mapClick.setOnClickListener(){
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=38.651481,-90.338292")
            )
            startActivity(intent)
        }
    }
}
