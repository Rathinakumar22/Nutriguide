package com.firstapp.nutriguide

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import java.util.Locale

class FoodRecommendationActivity : AppCompatActivity() {

    private lateinit var editTextDeficiency: EditText
    private lateinit var btnGetRecommendation: Button
    private lateinit var textViewRecommendation: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_food_recommendation)

        editTextDeficiency = findViewById(R.id.editTextDeficiency)
        btnGetRecommendation = findViewById(R.id.btnGetRecommendation)
        textViewRecommendation = findViewById(R.id.textViewRecommendation)

        firestore = FirebaseFirestore.getInstance()

        btnGetRecommendation.setOnClickListener {
            val deficiency = editTextDeficiency.text.toString().trim().replace("\\s+".toRegex(), "")
            if (isInputValid(deficiency)) {
                getFoodRecommendations(deficiency.lowercase(Locale.ROOT))
            } else {
                textViewRecommendation.text = getString(R.string.invalid_input_message)
            }
        }
    }

    private fun isInputValid(deficiency: String): Boolean {
        return deficiency.isNotEmpty() && deficiency.all { it.isLetter() || it.isWhitespace() }
    }

    private fun getFoodRecommendations(deficiency: String) {
        firestore.collection("foods")
            .whereEqualTo("recommended_for_deficiency", deficiency)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    textViewRecommendation.text = getString(R.string.no_recommendations_found)
                } else {
                    val recommendations = documents.joinToString { it.getString("name") ?: "" }
                    textViewRecommendation.text = getString(R.string.recommended_foods, recommendations)
                }
            }
            .addOnFailureListener { exception ->
                textViewRecommendation.text = getString(R.string.error_fetching_data, exception.message)
            }
    }
}