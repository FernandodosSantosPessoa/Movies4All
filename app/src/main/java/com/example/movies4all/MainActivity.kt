package com.example.movies4all

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

//    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
//        mainComponent = (applicationContext as MoviesApplication).appComponent.mainComponent().create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}