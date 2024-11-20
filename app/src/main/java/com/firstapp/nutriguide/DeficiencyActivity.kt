package com.firstapp.nutriguide

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import java.util.Locale

class DeficiencyActivity : AppCompatActivity() {

    private lateinit var editTextSymptoms: EditText
    private lateinit var btnIdentifyDeficiency: Button
    private lateinit var textViewInfo: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_deficiency_identifier)

        editTextSymptoms = findViewById(R.id.editTextSymptoms)
        btnIdentifyDeficiency = findViewById(R.id.btnIdentifyDeficiency)
        textViewInfo = findViewById(R.id.textViewInfo)
        firestore = FirebaseFirestore.getInstance()

        btnIdentifyDeficiency.setOnClickListener {
            val symptom = editTextSymptoms.text.toString().trim().replace("\\s+".toRegex(), "")
            if (isInputValid(symptom)) {
                identifyDeficiency(symptom.lowercase(Locale.ROOT))
            } else {
                textViewInfo.text = getString(R.string.invalid_input_message)
            }
        }
    }

    private fun isInputValid(symptom: String): Boolean {
        return symptom.isNotEmpty() && symptom.all { it.isLetter() || it.isWhitespace() }
    }

    private fun identifyDeficiency(symptom: String) {
        firestore.collection("symptoms")
            .whereEqualTo("symptom_name", symptom)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d("DeficiencyActivity", "No information found for symptom: $symptom")
                    textViewInfo.text = getString(R.string.no_information_found)
                } else {
                    val deficiencies =
                        documents.joinToString { it.getString("possible_deficiencies") ?: "" }
                    textViewInfo.text = getString(R.string.possible_deficiencies, deficiencies)
                    Log.d("DeficiencyActivity", "Deficiencies found: $deficiencies")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DeficiencyActivity", "Error getting documents: ", exception)
                textViewInfo.text = getString(R.string.error_fetching_data, exception.message)
            }
    }
}