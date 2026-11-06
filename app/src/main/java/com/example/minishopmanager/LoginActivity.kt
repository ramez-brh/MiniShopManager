package com.example.minishopmanager

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        const val PREFS = "MiniShopPrefs"        // nom du fichier SharedPreferences
        const val KEY_USER = "USERNAME"
        const val KEY_REMEMBER = "REMEMBER"
        const val EXTRA_USER = "username_key"    // clé de l'extra envoyé à MainActivity
    }

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE)

        // Remember Me : à la reconnexion, le champ utilisateur est prérempli
        etUsername.setText(prefs.getString(KEY_USER, ""))
        cbRemember.isChecked = prefs.getBoolean(KEY_REMEMBER, false)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            if (username.isEmpty() || etPassword.text.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir le nom et le mot de passe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Sauvegarder (ou effacer) les préférences selon la case
            val editor = prefs.edit()
            if (cbRemember.isChecked) {
                editor.putString(KEY_USER, username)
                editor.putBoolean(KEY_REMEMBER, true)
            } else {
                editor.clear()
            }
            editor.apply()

            // Ouvrir le catalogue en transmettant le nom d'utilisateur
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_USER, username)
            startActivity(intent)
            finish()   // on ne revient pas au login avec le bouton Retour
        }
    }
}
