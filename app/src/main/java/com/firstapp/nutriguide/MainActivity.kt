package com.firstapp.nutriguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    // Declare the LottieAnimationView variable
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        // Initialize the LottieAnimationView
        lottieAnimationView = findViewById(R.id.lottieAnimation)
        lottieAnimationView.playAnimation()

        val btnFood = findViewById<Button>(R.id.btnFood)
        val btnDeficiency = findViewById<Button>(R.id.btnDeficiency)

        btnFood.setOnClickListener {
            startActivity(Intent(this, FoodRecommendationActivity::class.java))
        }

        btnDeficiency.setOnClickListener {
            startActivity(Intent(this, DeficiencyActivity::class.java))
        }
    }
}