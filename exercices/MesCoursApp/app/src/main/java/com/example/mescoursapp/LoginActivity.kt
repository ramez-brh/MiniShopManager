package com.example.mescoursapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etStudentName = findViewById<EditText>(R.id.etStudentName)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        prefs = getSharedPreferences("CoursPrefs", MODE_PRIVATE)

        // Charger les préférences sauvegardées
        etStudentName.setText(prefs.getString("STUDENT", ""))
        cbRemember.isChecked = prefs.getBoolean("REMEMBER", false)

        btnLogin.setOnClickListener {
            val editor = prefs.edit()
            if (cbRemember.isChecked) {
                editor.putString("STUDENT", etStudentName.text.toString())
                editor.putBoolean("REMEMBER", true)
            } else {
                editor.clear()
            }
            editor.apply()

            // Ouvrir la liste des cours
            val intent = Intent(this, ListeCoursActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
