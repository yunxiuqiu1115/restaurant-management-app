package com.example.final_project.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.final_project.R
import com.example.final_project.adaptor.ViewPagerAdapter2
import com.example.final_project.fragments.CheckoutFragment
import com.example.final_project.util.Restaurant
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.pow


class HomeActivity : AppCompatActivity() {
    private lateinit var vpa : ViewPagerAdapter2
    private var resList:ArrayList<Restaurant> = ArrayList()


    private val PERMISSION_ID = 42
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    private var minDistanceRestaurantName =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        resList.add(Restaurant("0", "CC steak House Clayton",38.651481,-90.338292))
        resList.add(Restaurant("1", "CC steak House Downtown St Louis",38.633783,-90.199671))

        getLastLocation()
        menu_bottom.setOnItemSelectedListener{
            when(it){
                R.id.home -> viewpager.currentItem = 0
                R.id.menu -> viewpager.currentItem = 1
                R.id.profile -> viewpager.currentItem = 2
                R.id.accessory -> viewpager.currentItem = 3
            }
        }




        vpa = ViewPagerAdapter2(supportFragmentManager)
        viewpager.adapter = vpa
        viewpager.offscreenPageLimit = 3
        viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {

                when(position){
                    0 -> menu_bottom.setItemSelected(R.id.home)
                    1 -> menu_bottom.setItemSelected(R.id.menu)
                    2 -> menu_bottom.setItemSelected(R.id.profile)
                    3 -> menu_bottom.setItemSelected(R.id.accessory)
                }
            }

            // This method will be invoked when the current page is scrolled
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            override fun onPageScrollStateChanged(state: Int) {
                // Code goes here
            }
        })
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location.latitude
                        lon = location.longitude
                        Log.d("mylog", lat.toString())
                        Log.d("mylog", lon.toString())
                        getClosestRestaurant()

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }




        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude
            lon = mLastLocation.longitude
            Log.d("mylog", lat.toString())
            Log.d("mylog", lon.toString())
            getClosestRestaurant()

        }
    }


    private fun getClosestRestaurant(){
        var minDis = 9999.9
        var dis = 0.0

        for (res in resList){

            dis = kotlin.math.sqrt((res.lat - lat).pow(2)+(res.lon - lon).pow(2))
            if (dis < minDis){
                minDis = dis
                minDistanceRestaurantName = res.term

            }

        }
        Log.d("mylog", minDistanceRestaurantName)
        Toast.makeText(this, "$minDistanceRestaurantName is the closest branch to you!", Toast.LENGTH_LONG).show()

    }





}
