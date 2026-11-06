package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"   // étiquette pour filtrer Logcat
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate appelé")

        val btnSuivant = findViewById<Button>(R.id.btnSuivant)
        btnSuivant.setOnClickListener {
            // 1. Toast de bienvenue
            Toast.makeText(this, "Bonjour Ramez Barhoumi !", Toast.LENGTH_SHORT).show()
            // 2. Intent explicite vers l'écran Profil
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    // ----- Cycle de vie : chaque étape est tracée dans Logcat (filtre : MainActivity) -----
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart appelé")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume appelé")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause appelé")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop appelé")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart appelé")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy appelé")
    }
}
